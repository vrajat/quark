/*
 * Copyright (c) 2015. Qubole Inc
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.qubole.quark.server;

import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.jdbc.JdbcMeta;
import org.apache.calcite.avatica.remote.Driver;
import org.apache.calcite.avatica.remote.LocalService;
import org.apache.calcite.avatica.remote.Service;
import org.apache.calcite.avatica.server.HandlerFactory;
import org.apache.calcite.avatica.server.HttpServer;

import com.qubole.quark.server.configuration.QuarkConfiguration;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import org.eclipse.jetty.server.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Creates all DAOs and registers with te environment.
 */
public class QuarkApp extends Application<QuarkConfiguration> {
  private static final Logger LOG = LoggerFactory.getLogger(com.qubole.quark.server.QuarkApp.class);
  private static String url = "jdbc:quark:fat:db:";

  private final CountDownLatch runningLatch = new CountDownLatch(1);
  private HttpServer server = null;

  public static void main(String[] args) throws Exception {
    new com.qubole.quark.server.QuarkApp().run(args);
  }

  @Override
  public void run(QuarkConfiguration configuration,
                  Environment environment) {
    try {
      Properties properties = new Properties();
      properties.setProperty("url", configuration.dbCredentials.url);
      properties.setProperty("user", configuration.dbCredentials.username);
      properties.setProperty("password", configuration.dbCredentials.password);
      properties.setProperty("encryptionKey", configuration.dbCredentials.encryptionKey);

      Meta meta = new JdbcMeta(url, properties);

      LOG.debug("Listening on port " + configuration.port);
      final HandlerFactory handlerFactory = new HandlerFactory();
      Service service = new LocalService(meta);
      server = new HttpServer(configuration.port, getHandler(service, handlerFactory));

      Class.forName("com.qubole.quark.fatjdbc.QuarkDriver");
      server.start();
      runningLatch.countDown();
      server.join();
    } catch (Throwable t) {
      LOG.error("Unrecoverable service error. Shutting down.", t);
    }
  }

  /**
   * Instantiates the Handler for use by the Avatica (Jetty) server.
   *
   * @param service The Avatica Service implementation
   * @param handlerFactory Factory used for creating a Handler
   * @return The Handler to use.
   */
  Handler getHandler(Service service, HandlerFactory handlerFactory) {
    String serializationName = "PROTOBUF";
    Driver.Serialization serialization;
    try {
      serialization = Driver.Serialization.valueOf(serializationName);
    } catch (Exception e) {
      LOG.error("Unknown message serialization type for " + serializationName);
      throw e;
    }

    Handler handler = handlerFactory.getHandler(service, serialization);
    LOG.info("Instantiated " + handler.getClass() + " for Quark Server");

    return handler;
  }

}

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
package com.qubole.quark.server.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Stores the credentials of database provided in the JSON template.
 */
public class DbCredentials extends Configuration {
  @Valid
  @NotNull
  public final String url;

  @Valid
  @NotNull
  public final String username;

  @Valid
  @NotNull
  public final String password;

  @Valid
  @NotNull
  public final String encryptionKey;

  @JsonCreator
  public DbCredentials(@JsonProperty("url") String url,
                       @JsonProperty("username") String username,
                       @JsonProperty("password") String password,
                       @JsonProperty("encrypt_key") String encrpytionKey) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.encryptionKey = encrpytionKey;
  }
}


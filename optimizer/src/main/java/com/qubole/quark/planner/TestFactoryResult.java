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

package com.qubole.quark.planner;

import com.qubole.quark.QuarkException;

import java.util.List;

/**
 * Created by rajatv on 6/20/17.
 */
public class TestFactoryResult {
  public final List<QuarkSchema> schemas;
  public final MetadataSchema metadataSchema;
  public final QuarkSchema defaultSchema;

  public TestFactoryResult(List<QuarkSchema> quarkSchemas, MetadataSchema
      metadataSchema, QuarkSchema defaultSchema) throws QuarkException {
    if (!quarkSchemas.isEmpty() && defaultSchema == null) {
      throw new QuarkException("Default Schema is required");
    }
    this.schemas = quarkSchemas;
    this.defaultSchema = defaultSchema;
    if (metadataSchema != null) {
      this.metadataSchema = metadataSchema;
    } else {
      this.metadataSchema = MetadataSchema.empty();
    }
  }
}

package com.qubole.quark.planner.test;

import com.google.common.collect.ImmutableList;
import com.qubole.quark.QuarkException;
import com.qubole.quark.planner.QuarkSchema;
import com.qubole.quark.planner.TestFactory;
import com.qubole.quark.planner.TestFactoryResult;

import java.util.List;
import java.util.Properties;

/**
 * Created by rajatv on 2/21/16.
 */
public class PartialCubeSchemaFactory extends TestFactory {
  public PartialCubeSchemaFactory() {
     super(new Tpcds("TPCDS"));
  }

  public TestFactoryResult create(Properties info) throws QuarkException {
    PartialCubeSchema cubeSchema = new PartialCubeSchema(info.getProperty("filter"));
    return new TestFactoryResult(ImmutableList.of(this.getDefaultSchema()), cubeSchema, this.getDefaultSchema());
  }
}

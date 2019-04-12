package com.github.nhirakawa.hyperbeam.serde;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

public class HyperbeamModule extends Module {

  @Override
  public String getModuleName() {
    return "hyperbeam";
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addSerializers(new HyperbeamSerializers());
    context.addDeserializers(new HyperbeamDeserializers());
  }

}

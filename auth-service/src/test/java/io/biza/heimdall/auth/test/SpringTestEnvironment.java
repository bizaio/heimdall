package io.biza.heimdall.auth.test;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SpringTestEnvironment {
  
  public URI getIssuerUri() {
    return URI.create("https://localhost:" + System.getProperty("local.server.port") + "/dio-auth");
  }
}

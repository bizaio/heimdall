package io.biza.heimdall.auth.util;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class EndpointUtil {

  public static URI issuerUri() {
    return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());
  }

  public static URI tokenEndpoint() {
    return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/oidc/token");
  }


  public static URI jwksUri() {
    return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/jwks");
  }
}

package io.biza.heimdall.register.util;

import java.net.URI;
import java.util.List;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class EndpointUtil {

  public static URI issuerUri() {
    String hostName = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getHost();
    int hostPort = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPort();
    String hostScheme = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getScheme();
    return URI.create(String.join("", List.of(hostScheme, "://", hostName, "/")));
  }

  public static URI tokenEndpoint() {
    String hostName = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getHost();
    int hostPort = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPort();
    String hostScheme = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getScheme();
    return URI.create(String.join("",
        List.of(hostScheme, "://", hostName, ":", String.valueOf(hostPort), "/oauth2/token")));
  }


  public static URI jwksUri() {
    String hostName = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getHost();
    int hostPort = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPort();
    String hostScheme = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getScheme();
    return URI.create(String.join("",
        List.of(hostScheme, "://", hostName, ":", String.valueOf(hostPort), "/jwks")));
  }
}

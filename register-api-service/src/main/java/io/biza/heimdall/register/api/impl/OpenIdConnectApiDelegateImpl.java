package io.biza.heimdall.register.api.impl;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.OpenIdConnectApiDelegate;
import io.biza.thumb.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.thumb.oidc.enumerations.OAuth2ResponseType;
import io.biza.thumb.oidc.enumerations.OIDCAuthMethod;
import io.biza.thumb.oidc.enumerations.OIDCGrantType;
import io.biza.thumb.oidc.enumerations.OIDCSubjectType;
import io.biza.thumb.oidc.payloads.ProviderDiscoveryMetadata;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class OpenIdConnectApiDelegateImpl implements OpenIdConnectApiDelegate {
  
  @Value("${heimdall.hostname}")
  private String hostname;

  @Override
  public ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    
    String hostName = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getHost();
    int hostPort = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPort();
    String hostScheme = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getScheme();
    
    URI issuerUri = URI.create(String.join("", List.of(hostScheme, "://", hostName, "/")));
    URI tokenEndpoint = URI.create(String.join("", List.of(hostScheme, "://", hostName, ":", String.valueOf(hostPort), "/oauth2/token")));
    URI jwksUri = URI.create(String.join("", List.of(hostScheme, "://", hostName, ":", String.valueOf(hostPort), "/jwks")));
    
    
    return ResponseEntity.ok(ProviderDiscoveryMetadata.builder()
        .issuer(issuerUri)
        .jwksUri(jwksUri)
        .tokenEndpoint(tokenEndpoint)
        .claimsSupported(List.of("sub"))
        .idTokenSigningAlgorithms(List.of(JWSSigningAlgorithmType.PS256))
        .subjectTypesSupported(List.of(OIDCSubjectType.PUBLIC))
        .scopesSupported(Constants.REGISTER_SCOPE_LIST)
        .responseTypesSupported(List.of(List.of(OAuth2ResponseType.TOKEN)))
        .grantTypesSupported(List.of(OIDCGrantType.CLIENT_CREDENTIALS))
        .tokenEndpointAuthMethods(List.of(OIDCAuthMethod.PRIVATE_KEY_JWT))
        .oauthMtlsSupported(true)
        .requestObjectSigningAlgorithms(List.of(JWSSigningAlgorithmType.PS256))
        .requestUriSupport(null)
        .build());
  }

}

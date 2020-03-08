package io.biza.heimdall.auth.api.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.enumerations.OAuth2ResponseType;
import io.biza.babelfish.oidc.enumerations.OIDCAuthMethod;
import io.biza.babelfish.oidc.enumerations.OIDCGrantType;
import io.biza.babelfish.oidc.enumerations.OIDCSubjectType;
import io.biza.babelfish.oidc.payloads.JWKS;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.interfaces.JWKService;
import io.biza.heimdall.auth.api.delegate.DiscoveryApiDelegate;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.util.EndpointUtil;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class DiscoveryApiDelegateImpl implements DiscoveryApiDelegate {

  @Autowired
  RegisterAuthorityTLSRepository caRepository;
  
  @Autowired
  JWKService jwkService;
  
  @Override
  public ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {


    return ResponseEntity.ok(ProviderDiscoveryMetadata.builder().issuer(EndpointUtil.issuerUri())
        .jwksUri(EndpointUtil.jwksUri()).tokenEndpoint(EndpointUtil.tokenEndpoint())
        .claimsSupported(List.of("sub"))
        .idTokenSigningAlgorithms(List.of(JWSSigningAlgorithmType.PS256))
        .subjectTypesSupported(List.of(OIDCSubjectType.PUBLIC))
        .scopesSupported(Constants.REGISTER_SCOPE_LIST)
        .responseTypesSupported(List.of(List.of(OAuth2ResponseType.TOKEN)))
        .grantTypesSupported(List.of(OIDCGrantType.CLIENT_CREDENTIALS))
        .tokenEndpointAuthMethods(List.of(OIDCAuthMethod.PRIVATE_KEY_JWT)).oauthMtlsSupported(true)
        .requestObjectSigningAlgorithms(List.of(JWSSigningAlgorithmType.PS256))
        .requestUriSupport(null).build());
  }

  @Override
  public ResponseEntity<JWKS> getJwks() throws NotInitialisedException {
    return ResponseEntity.ok(jwkService.getJwks());
  }
}

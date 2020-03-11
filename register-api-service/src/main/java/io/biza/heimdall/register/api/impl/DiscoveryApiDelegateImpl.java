package io.biza.heimdall.register.api.impl;

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
import io.biza.heimdall.register.api.delegate.DiscoveryApiDelegate;
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
  public ResponseEntity<JWKS> getJwks() throws NotInitialisedException {
    return ResponseEntity.ok(jwkService.getJwks());
  }
}

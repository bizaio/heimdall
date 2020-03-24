package io.biza.heimdall.shared.component.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.biza.babelfish.oidc.payloads.JWKS;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.interfaces.JWKService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeyService {

  @Autowired
  JWKService jwkService;

  public JWKS getJwks() throws NotInitialisedException {
    return jwkService.getJwks();
  }

}

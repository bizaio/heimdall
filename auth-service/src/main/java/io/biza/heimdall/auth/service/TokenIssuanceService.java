package io.biza.heimdall.auth.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.enumerations.OAuth2TokenType;
import io.biza.babelfish.oidc.payloads.JWTClaims;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.interfaces.JWKService;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.util.EndpointUtil;
import io.biza.heimdall.shared.persistence.model.TokenData;
import io.biza.heimdall.shared.persistence.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenIssuanceService {

  @Autowired
  TokenRepository tokenRepository;

  @Autowired
  JWKService signingService;

  public ResponseEntity<TokenResponse> createAccessToken(TokenData token) throws SigningOperationException {

    TokenData savedToken = tokenRepository.save(token);
    
    LOG.debug("Saved token with details of: {}", savedToken);
    
    JWTClaims tokenClaims = JWTClaims.builder().subjectByUUID(token.client().id()).jwtId(token.id().toString()).expiry(savedToken.expiry()).issuedAt(savedToken.created())
    .issuerByURI(EndpointUtil.issuerUri()).audience(List.of(savedToken.audience())).scope(List.of(Constants.SECURITY_SCOPE_REGISTER_BANK_READ)).build();

    return ResponseEntity.ok(TokenResponse.builder()
        .accessToken(signingService.sign(tokenClaims, JWSSigningAlgorithmType.PS256)).expiresAt(savedToken.expiry())
        .scope(savedToken.scopes()).tokenType(OAuth2TokenType.BEARER).build());
  }


}

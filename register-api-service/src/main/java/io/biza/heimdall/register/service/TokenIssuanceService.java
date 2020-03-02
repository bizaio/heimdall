package io.biza.heimdall.register.service;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.heimdall.register.exceptions.CryptoException;
import io.biza.heimdall.register.exceptions.NotInitialisedException;
import io.biza.heimdall.register.util.EndpointUtil;
import io.biza.heimdall.shared.persistence.model.TokenData;
import io.biza.heimdall.shared.persistence.repository.TokenRepository;
import io.biza.thumb.oidc.enumerations.OAuth2AccessTokenType;
import io.biza.thumb.oidc.payloads.TokenResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenIssuanceService {

  @Autowired
  TokenRepository tokenRepository;

  @Autowired
  JwtSigningService signingService;

  public ResponseEntity<TokenResponse> createAccessToken(TokenData token)
      throws NotInitialisedException, CryptoException {

    TokenData savedToken = tokenRepository.save(token);

    /**
     * Build claim set
     */
    JwtClaims tokenClaims = new JwtClaims();
    tokenClaims.setJwtId(token.id().toString());
    tokenClaims.setExpirationTime(NumericDate.fromSeconds(savedToken.expiry().toEpochSecond()));
    tokenClaims.setIssuedAt(NumericDate.fromSeconds(savedToken.created().toEpochSecond()));
    tokenClaims.setIssuer(EndpointUtil.issuerUri().toString());
    tokenClaims.setAudience(savedToken.audience());
    tokenClaims.setClaim("typ", "Bearer");

    return ResponseEntity.ok(TokenResponse.builder()
        .accessToken(signingService.signData(tokenClaims.toJson())).expiresAt(savedToken.expiry())
        .scope(savedToken.scopes()).tokenType(OAuth2AccessTokenType.BEARER).build());
  }


}
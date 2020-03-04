package io.biza.heimdall.auth.service;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.babelfish.oidc.enumerations.OAuth2AccessTokenType;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
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
  JwtSigningService signingService;

  public ResponseEntity<TokenResponse> createAccessToken(TokenData token)
      throws NotInitialisedException, CryptoException {

    TokenData savedToken = tokenRepository.save(token);

    /**
     * Build claim set
     */
    JwtClaims tokenClaims = new JwtClaims();
    tokenClaims.setSubject(token.client().id().toString());
    tokenClaims.setJwtId(token.id().toString());
    tokenClaims.setExpirationTime(NumericDate.fromSeconds(savedToken.expiry().toEpochSecond()));
    tokenClaims.setIssuedAt(NumericDate.fromSeconds(savedToken.created().toEpochSecond()));
    tokenClaims.setIssuer(EndpointUtil.issuerUri().toString());
    tokenClaims.setAudience(savedToken.audience());
    tokenClaims.setClaim("typ", "Bearer");
    tokenClaims.setClaim("scope", Constants.SECURITY_SCOPE_REGISTER_BANK_READ);

    return ResponseEntity.ok(TokenResponse.builder()
        .accessToken(signingService.signData(tokenClaims)).expiresAt(savedToken.expiry())
        .scope(savedToken.scopes()).tokenType(OAuth2AccessTokenType.BEARER).build());
  }


}

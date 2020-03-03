package io.biza.heimdall.auth.service;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.babelfish.oidc.util.SigningUtil;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtSigningService {

  @Autowired
  RegisterAuthorityJWKRepository jwkRepository;

  public String signData(JwtClaims tokenClaims) throws NotInitialisedException, CryptoException {

    RegisterAuthorityJWKData jwkData = jwkRepository.findFirstByStatusIn(List.of(JWKStatus.ACTIVE));
    if (jwkData == null) {
      throw new NotInitialisedException();
    }
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(jwkData.javaFactory());
      PKCS8EncodedKeySpec privateKeySpec =
          new PKCS8EncodedKeySpec(Base64.getDecoder().decode(jwkData.privateKey()));
      PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

      PublicJsonWebKey jwk = PublicJsonWebKey.Factory.newPublicJwk(jwkData.publicKey());
      jwk.setPrivateKey(privateKey);
      jwk.setKeyId(jwkData.id().toString());

      return SigningUtil.sign(tokenClaims, jwk);

    } catch (NoSuchAlgorithmException | JoseException | InvalidKeySpecException | IOException e) {
      LOG.error("Encountered Crypto Exception while attempting signing", e);
      throw new CryptoException();
    }

  }

}

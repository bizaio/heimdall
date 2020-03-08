package io.biza.heimdall.auth.api.impl;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.enumerations.OAuth2ResponseType;
import io.biza.babelfish.oidc.enumerations.OIDCAuthMethod;
import io.biza.babelfish.oidc.enumerations.OIDCGrantType;
import io.biza.babelfish.oidc.enumerations.OIDCSubjectType;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.heimdall.auth.api.delegate.DiscoveryApiDelegate;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.util.EndpointUtil;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class DiscoveryApiDelegateImpl implements DiscoveryApiDelegate {

  @Autowired
  RegisterAuthorityJWKRepository jwkRepository;

  @Autowired
  RegisterAuthorityTLSRepository caRepository;
  
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
  public ResponseEntity<RawJson> getJwks() {

    List<RegisterAuthorityJWKData> registerData =
        jwkRepository.findByStatusIn(List.of(JWKStatus.ACTIVE));

    JsonWebKeySet jsonWebKeySet = new JsonWebKeySet();


    registerData.forEach(jwk -> {
      try {
        KeyFactory keyFactory = KeyFactory.getInstance(jwk.javaFactory());
        X509EncodedKeySpec publicKeySpec =
            new X509EncodedKeySpec(Base64.getDecoder().decode(jwk.publicKey()));
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PublicJsonWebKey webKey = PublicJsonWebKey.Factory.newPublicJwk(publicKey);
        webKey.setAlgorithm(jwk.joseAlgorithm());
        webKey.setKeyId(jwk.id().toString());
        jsonWebKeySet.addJsonWebKey(webKey);
      } catch (JoseException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        LOG.error("Received error while parsing JWK from Database: {}", e.getMessage());
      }
    });

    return ResponseEntity.ok(RawJson.from(jsonWebKeySet.toJson(OutputControlLevel.PUBLIC_ONLY)));

  }
}

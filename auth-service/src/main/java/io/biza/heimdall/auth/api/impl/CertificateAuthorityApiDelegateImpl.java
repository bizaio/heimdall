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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.auth.api.delegate.CertificateAuthorityApiDelegate;
import io.biza.heimdall.payload.enumerations.CertificateStatus;
import io.biza.heimdall.payload.enumerations.JWKStatus;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class CertificateAuthorityApiDelegateImpl implements CertificateAuthorityApiDelegate {

  @Autowired
  RegisterAuthorityJWKRepository jwkRepository;

  @Autowired
  RegisterAuthorityTLSRepository caRepository;

  @Override
  public ResponseEntity<String> getCertificateAuthority() {
    RegisterAuthorityTLSData caData =
        caRepository.findFirstByStatusIn(List.of(CertificateStatus.ACTIVE));

    if (caData != null) {
      String publicCertificate = new StringBuilder().append("-----BEGIN CERTIFICATE-----\n")
          .append(caData.publicKey().replaceAll(".{80}(?=.)", "$0\n"))
          .append("\n-----END CERTIFICATE-----\n").toString();
      return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(publicCertificate);
    } else {
      LOG.error("Unable to provide CA public key when CA is not initialised");
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }


  }

}

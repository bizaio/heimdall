package io.biza.heimdall.auth.api.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.enumerations.register.CertificateStatus;
import io.biza.heimdall.auth.api.delegate.CertificateAuthorityApiDelegate;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class CertificateAuthorityApiDelegateImpl implements CertificateAuthorityApiDelegate {

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

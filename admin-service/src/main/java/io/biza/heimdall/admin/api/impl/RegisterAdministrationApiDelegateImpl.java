package io.biza.heimdall.admin.api.impl;

import io.biza.heimdall.admin.api.delegate.RegisterAdministrationApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import io.biza.babelfish.cdr.exceptions.EncryptionOperationException;
import io.biza.babelfish.cdr.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.interfaces.CertificateService;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class RegisterAdministrationApiDelegateImpl implements RegisterAdministrationApiDelegate {

  @Autowired
  CertificateService certificateService;

  @Override
  public ResponseEntity<String> signCertificate(RequestCACertificateSign createRequest) {

    try {
      return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
          .body(certificateService.signRequest(createRequest.csr()));
    } catch (NotInitialisedException | EncryptionOperationException e) {
      LOG.error("Attempt to sign certificate failed: {}", e);
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }
}

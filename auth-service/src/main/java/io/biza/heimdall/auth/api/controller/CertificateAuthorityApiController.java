package io.biza.heimdall.auth.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.auth.api.CertificateAuthorityApi;
import io.biza.heimdall.auth.api.delegate.CertificateAuthorityApiDelegate;

@Controller
public class CertificateAuthorityApiController implements CertificateAuthorityApi {

  private final CertificateAuthorityApiDelegate delegate;


  public CertificateAuthorityApiController(@Autowired(required = false) CertificateAuthorityApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new CertificateAuthorityApiDelegate() {

    });
  }

  @Override
  public CertificateAuthorityApiDelegate getDelegate() {
    return delegate;
  }

}

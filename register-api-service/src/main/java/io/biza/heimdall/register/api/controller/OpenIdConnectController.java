package io.biza.heimdall.register.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.register.api.OpenIdConnectApi;
import io.biza.heimdall.register.api.delegate.OpenIdConnectApiDelegate;

@Controller
public class OpenIdConnectController implements OpenIdConnectApi {

  private final OpenIdConnectApiDelegate delegate;


  public OpenIdConnectController(@Autowired(required = false) OpenIdConnectApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new OpenIdConnectApiDelegate() {

    });
  }

  @Override
  public OpenIdConnectApiDelegate getDelegate() {
    return delegate;
  }

}

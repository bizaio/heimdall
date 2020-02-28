package io.biza.heimdall.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.admin.api.BankingDataHolderApi;
import io.biza.heimdall.admin.api.RegisterAdministrationApi;
import io.biza.heimdall.admin.api.delegate.RegisterAdministrationApiDelegate;

@Controller
public class RegisterAdministrationApiController implements RegisterAdministrationApi {

  private final RegisterAdministrationApiDelegate delegate;


  public RegisterAdministrationApiController(@Autowired(required = false) RegisterAdministrationApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new RegisterAdministrationApiDelegate() {

    });
  }

  @Override
  public RegisterAdministrationApiDelegate getDelegate() {
    return delegate;
  }

}

package io.biza.heimdall.register.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.register.api.RegisterApi;
import io.biza.heimdall.register.api.delegate.RegisterApiDelegate;

@Controller
public class RegisterApiController implements RegisterApi {

  private final RegisterApiDelegate delegate;


  public RegisterApiController(@Autowired(required = false) RegisterApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new RegisterApiDelegate() {

    });
  }

  @Override
  public RegisterApiDelegate getDelegate() {
    return delegate;
  }

}

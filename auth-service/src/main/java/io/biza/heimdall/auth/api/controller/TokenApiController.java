package io.biza.heimdall.auth.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.auth.api.TokenApi;
import io.biza.heimdall.auth.api.delegate.TokenApiDelegate;

@Controller
public class TokenApiController implements TokenApi {

  private final TokenApiDelegate delegate;


  public TokenApiController(@Autowired(required = false) TokenApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new TokenApiDelegate() {

    });
  }

  @Override
  public TokenApiDelegate getDelegate() {
    return delegate;
  }

}

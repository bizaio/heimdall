package io.biza.heimdall.auth.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.auth.api.DiscoveryApi;
import io.biza.heimdall.auth.api.delegate.DiscoveryApiDelegate;

@Controller
public class DiscoveryApiController implements DiscoveryApi {

  private final DiscoveryApiDelegate delegate;


  public DiscoveryApiController(@Autowired(required = false) DiscoveryApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new DiscoveryApiDelegate() {

    });
  }

  @Override
  public DiscoveryApiDelegate getDelegate() {
    return delegate;
  }

}

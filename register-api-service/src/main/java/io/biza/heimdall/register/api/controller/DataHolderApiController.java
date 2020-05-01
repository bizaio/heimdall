package io.biza.heimdall.register.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.register.api.DataHolderApi;
import io.biza.heimdall.register.api.delegate.DataHolderApiDelegate;

@Controller
public class DataHolderApiController implements DataHolderApi {

  private final DataHolderApiDelegate delegate;


  public DataHolderApiController(
      @Autowired(required = false) DataHolderApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new DataHolderApiDelegate() {

    });
  }

  @Override
  public DataHolderApiDelegate getDelegate() {
    return delegate;
  }

}

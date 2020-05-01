package io.biza.heimdall.register.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.register.api.DataRecipientApi;
import io.biza.heimdall.register.api.delegate.DataRecipientApiDelegate;

@Controller
public class DataRecipientApiController implements DataRecipientApi {

  private final DataRecipientApiDelegate delegate;


  public DataRecipientApiController(
      @Autowired(required = false) DataRecipientApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new DataRecipientApiDelegate() {

    });
  }

  @Override
  public DataRecipientApiDelegate getDelegate() {
    return delegate;
  }

}

package io.biza.heimdall.register.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.register.api.BankingDataRecipientApi;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;

@Controller
public class BankingDataRecipientApiController implements BankingDataRecipientApi {

  private final BankingDataRecipientApiDelegate delegate;


  public BankingDataRecipientApiController(@Autowired(required = false) BankingDataRecipientApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new BankingDataRecipientApiDelegate() {

    });
  }

  @Override
  public BankingDataRecipientApiDelegate getDelegate() {
    return delegate;
  }

}

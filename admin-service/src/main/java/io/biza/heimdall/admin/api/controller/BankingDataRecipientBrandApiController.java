package io.biza.heimdall.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.admin.api.BankingDataRecipientBrandApi;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;

@Controller
public class BankingDataRecipientBrandApiController implements BankingDataRecipientBrandApi {

  private final BankingDataRecipientBrandApiDelegate delegate;


  public BankingDataRecipientBrandApiController(
      @Autowired(required = false) BankingDataRecipientBrandApiDelegate delegate) {
    this.delegate =
        Optional.ofNullable(delegate).orElse(new BankingDataRecipientBrandApiDelegate() {

        });
  }

  @Override
  public BankingDataRecipientBrandApiDelegate getDelegate() {
    return delegate;
  }

}

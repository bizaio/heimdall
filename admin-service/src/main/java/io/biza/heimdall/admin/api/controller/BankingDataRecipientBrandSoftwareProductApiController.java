package io.biza.heimdall.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.admin.api.BankingDataRecipientBrandSoftwareProductApi;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandSoftwareProductApiDelegate;

@Controller
public class BankingDataRecipientBrandSoftwareProductApiController
    implements BankingDataRecipientBrandSoftwareProductApi {

  private final BankingDataRecipientBrandSoftwareProductApiDelegate delegate;


  public BankingDataRecipientBrandSoftwareProductApiController(
      @Autowired(required = false) BankingDataRecipientBrandSoftwareProductApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate)
        .orElse(new BankingDataRecipientBrandSoftwareProductApiDelegate() {

        });
  }

  @Override
  public BankingDataRecipientBrandSoftwareProductApiDelegate getDelegate() {
    return delegate;
  }

}

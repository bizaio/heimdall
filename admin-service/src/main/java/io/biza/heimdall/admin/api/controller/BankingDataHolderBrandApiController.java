package io.biza.heimdall.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.admin.api.BankingDataHolderBrandApi;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderBrandApiDelegate;

@Controller
public class BankingDataHolderBrandApiController implements BankingDataHolderBrandApi {

  private final BankingDataHolderBrandApiDelegate delegate;


  public BankingDataHolderBrandApiController(
      @Autowired(required = false) BankingDataHolderBrandApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new BankingDataHolderBrandApiDelegate() {

    });
  }

  @Override
  public BankingDataHolderBrandApiDelegate getDelegate() {
    return delegate;
  }

}

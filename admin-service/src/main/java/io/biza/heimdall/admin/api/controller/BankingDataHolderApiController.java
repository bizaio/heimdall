package io.biza.heimdall.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.admin.api.BankingDataHolderApi;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderApiDelegate;

@Controller
public class BankingDataHolderApiController implements BankingDataHolderApi {

  private final BankingDataHolderApiDelegate delegate;


  public BankingDataHolderApiController(@Autowired(required = false) BankingDataHolderApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new BankingDataHolderApiDelegate() {

    });
  }

  @Override
  public BankingDataHolderApiDelegate getDelegate() {
    return delegate;
  }

}

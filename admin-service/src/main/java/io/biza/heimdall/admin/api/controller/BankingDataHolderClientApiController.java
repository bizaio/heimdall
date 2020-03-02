package io.biza.heimdall.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.heimdall.admin.api.BankingDataHolderClientApi;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderClientApiDelegate;

@Controller
public class BankingDataHolderClientApiController implements BankingDataHolderClientApi {

  private final BankingDataHolderClientApiDelegate delegate;


  public BankingDataHolderClientApiController(
      @Autowired(required = false) BankingDataHolderClientApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new BankingDataHolderClientApiDelegate() {

    });
  }

  @Override
  public BankingDataHolderClientApiDelegate getDelegate() {
    return delegate;
  }

}

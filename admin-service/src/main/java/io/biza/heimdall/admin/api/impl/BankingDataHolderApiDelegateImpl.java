package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import io.biza.babelfish.cdr.exceptions.NotFoundException;
import io.biza.babelfish.cdr.exceptions.ValidationListException;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderApiDelegate;
import io.biza.heimdall.shared.component.service.HolderService;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;

@Validated
@Controller
public class BankingDataHolderApiDelegateImpl implements BankingDataHolderApiDelegate {
  
  @Autowired
  HolderService holderService;

  @Override
  public ResponseEntity<DioDataHolder> createHolder(DioDataHolder holder) throws ValidationListException {
    return ResponseEntity.ok(holderService.create(holder));
  }

  @Override
  public ResponseEntity<List<DioDataHolder>> listHolders() {
    return ResponseEntity.ok(holderService.list(null, null).toList());
  }

  @Override
  public ResponseEntity<DioDataHolder> getHolder(UUID holderId) throws NotFoundException {
    return ResponseEntity.ok(holderService.read(holderId));
  }

  @Override
  public ResponseEntity<DioDataHolder> updateHolder(UUID holderId, DioDataHolder updateData) throws ValidationListException, NotFoundException {
    return ResponseEntity.ok(holderService.update(holderId, updateData));
  }

  @Override
  public ResponseEntity<Void> deleteHolder(UUID holderId) throws NotFoundException {
    holderService.delete(holderId);
    return ResponseEntity.noContent().build();
  }
}

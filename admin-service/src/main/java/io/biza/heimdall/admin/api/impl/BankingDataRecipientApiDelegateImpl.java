package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.service.RecipientService;
import io.biza.heimdall.shared.exceptions.NotFoundException;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {
  
  @Autowired
  RecipientService recipientService;

  @Override
  public ResponseEntity<DioDataRecipient> createRecipient(DioDataRecipient recipient) throws ValidationListException {
    return ResponseEntity.ok(recipientService.create(recipient));
  }

  @Override
  public ResponseEntity<List<DioDataRecipient>> listRecipients() {
    return ResponseEntity.ok(recipientService.list(null, null).toList());
  }

  @Override
  public ResponseEntity<DioDataRecipient> getRecipient(UUID recipientId) throws NotFoundException {
    return ResponseEntity.ok(recipientService.read(recipientId));
  }

  @Override
  public ResponseEntity<DioDataRecipient> updateRecipient(UUID recipientId,
      DioDataRecipient updateData) throws ValidationListException, NotFoundException {
    return ResponseEntity.ok(recipientService.update(recipientId, updateData));
  }

  @Override
  public ResponseEntity<Void> deleteRecipient(UUID recipientId) throws NotFoundException {
    recipientService.delete(recipientId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

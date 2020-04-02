package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderClientApiDelegate;
import io.biza.heimdall.shared.component.service.ClientService;
import io.biza.heimdall.shared.exceptions.NotFoundException;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderClient;
import io.biza.heimdall.shared.persistence.specifications.ClientSpecifications;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderClientApiDelegateImpl implements BankingDataHolderClientApiDelegate {

  @Autowired
  ClientService clientService;

  @Override
  public ResponseEntity<DioDataHolderClient> createHolderClient(UUID holderId,
      DioDataHolderClient client) throws ValidationListException, NotFoundException {
    return ResponseEntity.ok(clientService.create(holderId, client));
  }

  @Override
  public ResponseEntity<List<DioDataHolderClient>> listHolderClients(UUID holderId) {

    return ResponseEntity
        .ok(clientService.list(ClientSpecifications.holderId(holderId), null).toList());
  }

  @Override
  public ResponseEntity<DioDataHolderClient> getHolderClient(UUID holderId, UUID clientId)
      throws NotFoundException {

    return ResponseEntity.ok(clientService.read(holderId, clientId));
  }

  @Override
  public ResponseEntity<DioDataHolderClient> updateHolderClient(UUID holderId, UUID clientId,
      DioDataHolderClient updateData) throws ValidationListException, NotFoundException {

    return ResponseEntity.ok(clientService.update(holderId, clientId, updateData));
  }

  @Override
  public ResponseEntity<Void> deleteHolderClient(UUID holderId, UUID clientId)
      throws NotFoundException {

    clientService.delete(holderId, clientId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

  }
}

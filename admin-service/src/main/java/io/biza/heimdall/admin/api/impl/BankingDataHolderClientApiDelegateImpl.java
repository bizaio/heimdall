package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderClientApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderClient;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderClientApiDelegateImpl implements BankingDataHolderClientApiDelegate {

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioDataHolderClient> createHolderClient(UUID holderId,
      DioDataHolderClient client) throws ValidationListException {

    Optional<DataHolderData> holder = holderRepository.findById(holderId);

    if (!holder.isPresent()) {
      LOG.warn("Attempted to create a holder client for a holder id of {} that doesn't exist",
          holderId);
      throw ValidationListException.builder().type(HeimdallExceptionType.INVALID_HOLDER)
          .explanation(Constants.ERROR_INVALID_HOLDER).build();
    }
    ClientData dataHolderData = mapper.map(client, ClientData.class);
    dataHolderData.id(UUID.randomUUID());
    dataHolderData.dataHolder(holder.get());
    ClientData savedClient = clientRepository.save(dataHolderData);
    LOG.debug("Created a new data holder with content of: {}", savedClient);
    return ResponseEntity.ok(mapper.map(savedClient, DioDataHolderClient.class));
  }

  @Override
  public ResponseEntity<List<DioDataHolderClient>> listHolderClients(UUID holderId) {

    Optional<DataHolderData> holder = holderRepository.findById(holderId);

    if (!holder.isPresent()) {
      LOG.warn("Attempted to list clients on non existent holder {}", holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    LOG.debug("Listing all data holders and received {}", holder.get().clients());
    return ResponseEntity.ok(mapper.mapAsList(
        Optional.of(holder.get().clients()).orElse(Set.of()), DioDataHolderClient.class));
  }

  @Override
  public ResponseEntity<DioDataHolderClient> getHolderClient(UUID holderId, UUID clientId) {
    Optional<ClientData> data =
        clientRepository.findByIdAndDataHolderId(clientId, holderId);

    if (data.isPresent()) {
      LOG.info(
          "Retrieving a single data holder client with holder of {} and client of {} and got content of {}",
          holderId, clientId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioDataHolderClient.class));
    } else {
      LOG.warn("Attempted to retrieve a single holder {} with client of {} and couldn't find it",
          holderId, clientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioDataHolderClient> updateHolderClient(UUID holderId, UUID clientId,
      DioDataHolderClient updateData) {
    Optional<ClientData> optionalData =
        clientRepository.findByIdAndDataHolderId(clientId, holderId);

    if (optionalData.isPresent()) {
      ClientData data = optionalData.get();
      mapper.map(updateData, data);
      data.dataHolder(optionalData.get().dataHolder());
      ClientData updatedData = clientRepository.save(data);

      LOG.info("Updating a single data holder client of holder {} and client {} and now set to {}",
          holderId, clientId, updatedData);

      return ResponseEntity.ok(mapper.map(updatedData, DioDataHolderClient.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single data holder client and could not find with holder of {} and client of {}",
          holderId, clientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteHolderClient(UUID holderId, UUID clientId) {
    Optional<ClientData> data =
        clientRepository.findByIdAndDataHolderId(clientId, holderId);

    if (data.isPresent()) {
      LOG.info("Deleting a single data holder client with id of {}", holderId);
      clientRepository.delete(data.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to retrieve a single data holder client and could not find with holder of {} and client of {}",
          holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}

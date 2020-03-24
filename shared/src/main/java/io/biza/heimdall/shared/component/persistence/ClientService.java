package io.biza.heimdall.shared.component.persistence;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.heimdall.shared.Messages;
import io.biza.heimdall.shared.component.functions.ValidationService;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.exceptions.NotFoundException;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderClient;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.heimdall.shared.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientService {

  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  private HeimdallMapper mapper;

  public static final String TYPE_NAME_PAYLOAD = DioDataHolderClient.class.getName();
  public static final String TYPE_NAME_DB = ClientData.class.getName();

  public DioDataHolderClient create(UUID holderId, DioDataHolderClient holderClient)
      throws ValidationListException, NotFoundException {

    /**
     * Locate existing holder
     */
    DataHolderData holderData =
        holderRepository.findById(holderId).orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_ID, holderId)).build());

    /**
     * Validate input data
     */
    validationService.validate(holderClient, MessageUtil
        .format(Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, holderClient));

    /**
     * Create Data Holder Client Record
     */
    ClientData dataHolderClientData =
        mapper.map(holderClient, ClientData.class).dataHolder(holderData);
    ClientData savedDataHolderClient = clientRepository.save(dataHolderClientData);
    LOG.debug(MessageUtil.format(Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataHolderClient));
    return mapper.map(savedDataHolderClient, DioDataHolderClient.class);
  }

  public Page<DioDataHolderClient> list(Specification<ClientData> specification,
      Pageable pageable) {

    if (specification == null) {
      specification = Specification.where(null);
    }

    Page<ClientData> dataHolderClientData;

    /**
     * List all hodlers
     */
    if (pageable != null) {
      dataHolderClientData = clientRepository.findAll(specification, pageable);
    } else {
      dataHolderClientData = new PageImpl<ClientData>(clientRepository.findAll(specification));
    }

    LOG.debug(MessageUtil.format(Messages.LIST_ALL_GENERIC_AND_RECEIVED, TYPE_NAME_DB,
        dataHolderClientData));

    /**
     * Reconstruct Page
     */
    Page<DioDataHolderClient> page = new PageImpl<DioDataHolderClient>(
        mapper.mapAsList(dataHolderClientData.getContent(), DioDataHolderClient.class),
        dataHolderClientData.getPageable(), dataHolderClientData.getTotalElements());

    /**
     * Map as a list
     */
    return page;
  }

  public DioDataHolderClient update(UUID holderId, UUID clientId, DioDataHolderClient holderClient)
      throws ValidationListException, NotFoundException {
    /**
     * Rewrite holder id
     */
    holderClient.id(clientId);

    /**
     * Validate input data
     */
    validationService.validate(holderClient, MessageUtil
        .format(Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, holderClient));

    /**
     * Locate existing holder
     */
    ClientData holderClientData =
        clientRepository.findByIdAndDataHolderId(clientId, holderId)
            .orElseThrow(() -> NotFoundException.builder().message(
                MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_CLIENT_ID, holderId, clientId))
                .build());

    /**
     * Map supplied data over the top
     */
    mapper.map(holderClient, holderClientData);
    ClientData savedDataHolderClient = clientRepository.save(holderClientData);
    LOG.debug(MessageUtil.format(Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataHolderClient));
    return mapper.map(savedDataHolderClient, DioDataHolderClient.class);
  }

  public DioDataHolderClient read(UUID holderId, UUID clientId) throws NotFoundException {

    /**
     * Locate existing holder
     */
    ClientData holderData =
        clientRepository.findByIdAndDataHolderId(clientId, holderId)
            .orElseThrow(() -> NotFoundException.builder().message(
                MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_CLIENT_ID, holderId, clientId))
                .build());

    return mapper.map(holderData, DioDataHolderClient.class);

  }

  public void delete(UUID holderId, UUID clientId) throws NotFoundException {

    /**
     * Locate existing holder
     */
    ClientData holderClientData =
        clientRepository.findByIdAndDataHolderId(clientId, holderId)
            .orElseThrow(() -> NotFoundException.builder().message(
                MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_CLIENT_ID, holderId, clientId))
                .build());

    /**
     * Now delete them
     */
    clientRepository.delete(holderClientData);

  }


}

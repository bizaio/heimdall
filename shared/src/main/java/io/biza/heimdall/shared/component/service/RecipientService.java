package io.biza.heimdall.shared.component.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.babelfish.spring.service.common.ValidationService;
import io.biza.heimdall.shared.Messages;
import io.biza.heimdall.shared.component.support.HeimdallMapper;
import io.biza.babelfish.cdr.util.MessageUtil;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
import io.biza.heimdall.shared.payloads.dio.DioSoftwareProduct;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipientService {

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  private HeimdallMapper mapper;

  public static final String TYPE_NAME_PAYLOAD = DioSoftwareProduct.class.getName();
  public static final String TYPE_NAME_DB = SoftwareProductData.class.getName();

  public DioDataRecipient create(DioDataRecipient recipient) throws ValidationListException {

    /**
     * Validate input data
     */
    validationService.validate(recipient, MessageUtil
        .format(io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, recipient));


    /**
     * Create Data Recipient Record
     */
    DataRecipientData dataRecipientData = mapper.map(recipient, DataRecipientData.class);
    DataRecipientData savedDataRecipient = recipientRepository.save(dataRecipientData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataRecipient));
    return mapper.map(savedDataRecipient, DioDataRecipient.class);
  }

  public Page<DioDataRecipient> list(Specification<DataRecipientData> specification,
      Pageable pageable) {

    if (specification == null) {
      specification = Specification.where(null);
    }

    Page<DataRecipientData> data;

    /**
     * List all recipients
     */
    if (pageable != null) {
      data = recipientRepository.findAll(specification, pageable);
    } else {
      data = new PageImpl<DataRecipientData>(recipientRepository.findAll(specification));
    }

    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.LIST_ALL_GENERIC_AND_RECEIVED, TYPE_NAME_DB, data));

    /**
     * Reconstruct Page
     */
    Page<DioDataRecipient> page =
        new PageImpl<DioDataRecipient>(mapper.mapAsList(data.getContent(), DioDataRecipient.class),
            data.getPageable(), data.getTotalElements());

    /**
     * Map as a list
     */
    return page;
  }

  public DioDataRecipient update(UUID recipientId, DioDataRecipient recipient)
      throws ValidationListException, NotFoundException {
    /**
     * Rewrite recipient id
     */
    recipient.id(recipientId);

    /**
     * Validate input data
     */
    validationService.validate(recipient, MessageUtil
        .format(io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, recipient));

    /**
     * Locate existing recipient
     */
    DataRecipientData recipientData = recipientRepository.findById(recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_ID, recipientId))
            .build());

    /**
     * Map supplied data over the top
     */
    mapper.map(recipient, recipientData);
    DataRecipientData savedDataRecipient = recipientRepository.save(recipientData);
    LOG.debug(
        MessageUtil.format(io.biza.babelfish.spring.Messages.UPDATED_GENERIC_WITH_CONTENT, TYPE_NAME_DB, recipientData));
    return mapper.map(savedDataRecipient, DioDataRecipient.class);
  }

  public DioDataRecipient read(UUID recipientId) throws NotFoundException {

    /**
     * Locate existing recipient
     */
    DataRecipientData recipientData = recipientRepository.findById(recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_ID, recipientId))
            .build());

    return mapper.map(recipientData, DioDataRecipient.class);

  }

  public void delete(UUID recipientId) throws NotFoundException {

    /**
     * Locate existing recipient
     */
    DataRecipientData recipientData = recipientRepository.findById(recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_ID, recipientId))
            .build());

    /**
     * Now delete them
     */
    recipientRepository.delete(recipientData);

  }


}

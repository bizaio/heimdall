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
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipientBrandService {

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  DataRecipientBrandRepository recipientBrandRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  private HeimdallMapper mapper;

  public static final String TYPE_NAME_PAYLOAD = DioDataRecipientBrand.class.getName();
  public static final String TYPE_NAME_DB = DataRecipientBrandData.class.getName();

  public DioDataRecipientBrand create(UUID recipientId, DioDataRecipientBrand recipientBrand)
      throws ValidationListException, NotFoundException {

    /**
     * Locate existing recipient
     */
    DataRecipientData recipientData = recipientRepository.findById(recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_ID, recipientId))
            .build());

    /**
     * Validate input data
     */
    validationService.validate(recipientBrand, MessageUtil.format(
        io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, recipientBrand));

    /**
     * Create Data Recipient Brand Record
     */
    DataRecipientBrandData dataRecipientBrandData =
        mapper.map(recipientBrand, DataRecipientBrandData.class).dataRecipient(recipientData);
    DataRecipientBrandData savedDataRecipientBrand =
        recipientBrandRepository.save(dataRecipientBrandData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataRecipientBrand));
    return mapper.map(savedDataRecipientBrand, DioDataRecipientBrand.class);
  }

  public Page<DioDataRecipientBrand> list(Specification<DataRecipientBrandData> specification,
      Pageable pageable) {

    if (specification == null) {
      specification = Specification.where(null);
    }

    Page<DataRecipientBrandData> data;

    /**
     * List all recipients
     */
    if (pageable != null) {
      data = recipientBrandRepository.findAll(specification, pageable);
    } else {
      data = new PageImpl<DataRecipientBrandData>(recipientBrandRepository.findAll(specification));
    }

    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.LIST_ALL_GENERIC_AND_RECEIVED, TYPE_NAME_DB, data));

    /**
     * Reconstruct Page
     */
    Page<DioDataRecipientBrand> page = new PageImpl<DioDataRecipientBrand>(
        mapper.mapAsList(data.getContent(), DioDataRecipientBrand.class), data.getPageable(),
        data.getTotalElements());

    /**
     * Map as a list
     */
    return page;
  }

  public DioDataRecipientBrand update(UUID recipientId, UUID brandId,
      DioDataRecipientBrand recipientBrand) throws ValidationListException, NotFoundException {
    /**
     * Rewrite recipient id
     */
    recipientBrand.id(brandId);

    /**
     * Validate input data
     */
    validationService.validate(recipientBrand, MessageUtil.format(
        io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, recipientBrand));

    /**
     * Locate existing recipient
     */
    DataRecipientBrandData recipientBrandData = recipientBrandRepository
        .findByIdAndDataRecipientId(brandId, recipientId)
        .orElseThrow(() -> NotFoundException.builder().message(
            MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_ID, recipientId, brandId))
            .build());

    /**
     * Map supplied data over the top
     */
    mapper.map(recipientBrand, recipientBrandData);
    DataRecipientBrandData savedDataRecipientBrand =
        recipientBrandRepository.save(recipientBrandData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.UPDATED_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataRecipientBrand));
    return mapper.map(savedDataRecipientBrand, DioDataRecipientBrand.class);
  }

  public DioDataRecipientBrand read(UUID recipientId, UUID brandId) throws NotFoundException {

    /**
     * Locate existing recipient
     */
    DataRecipientBrandData recipientData = recipientBrandRepository
        .findByIdAndDataRecipientId(brandId, recipientId)
        .orElseThrow(() -> NotFoundException.builder().message(
            MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_ID, recipientId, brandId))
            .build());

    return mapper.map(recipientData, DioDataRecipientBrand.class);

  }

  public void delete(UUID recipientId, UUID brandId) throws NotFoundException {

    /**
     * Locate existing recipient
     */
    DataRecipientBrandData recipientBrandData = recipientBrandRepository
        .findByIdAndDataRecipientId(brandId, recipientId)
        .orElseThrow(() -> NotFoundException.builder().message(
            MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_ID, recipientId, brandId))
            .build());

    /**
     * Now delete them
     */
    recipientBrandRepository.delete(recipientBrandData);

  }


}

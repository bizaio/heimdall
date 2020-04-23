package io.biza.heimdall.shared.component.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.biza.babelfish.cdr.util.MessageUtil;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.babelfish.spring.service.common.OrikaMapperService;
import io.biza.babelfish.spring.service.common.ValidationService;
import io.biza.heimdall.shared.Messages;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HolderService {

  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  OrikaMapperService mapper;

  public static final String TYPE_NAME_PAYLOAD = DioDataHolder.class.getName();
  public static final String TYPE_NAME_DB = DataHolderData.class.getName();

  public DioDataHolder create(DioDataHolder holder) throws ValidationListException {
    /**
     * Validate input data
     */
    validationService.validate(holder, MessageUtil
        .format(io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, holder));

    /**
     * Create Data Holder Record
     */
    DataHolderData dataHolderData = holderRepository.save(mapper.map(holder, DataHolderData.class));
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
    		dataHolderData));
    return mapper.map(dataHolderData, DioDataHolder.class);
  }

  public Page<DioDataHolder> list(Specification<DataHolderData> specification, Pageable pageable) {

    if (specification == null) {
      specification = Specification.where(null);
    }

    Page<DataHolderData> dataHolderData;

    /**
     * List all hodlers
     */
    if (pageable != null) {
      dataHolderData = holderRepository.findAll(specification, pageable);
    } else {
      dataHolderData = new PageImpl<DataHolderData>(holderRepository.findAll(specification));
    }

    LOG.debug(
        MessageUtil.format(io.biza.babelfish.spring.Messages.LIST_ALL_GENERIC_AND_RECEIVED, TYPE_NAME_DB, dataHolderData.getContent()));

    /**
     * Reconstruct Page
     */
    Page<DioDataHolder> page = new PageImpl<DioDataHolder>(
        mapper.mapAsList(dataHolderData.getContent(), DioDataHolder.class),
        dataHolderData.getPageable(), dataHolderData.getTotalElements());

    LOG.debug(
            MessageUtil.format(io.biza.babelfish.spring.Messages.LIST_ALL_GENERIC_AND_RECEIVED, TYPE_NAME_PAYLOAD, page.getContent()));

    /**
     * Map as a list
     */
    return page;
  }

  public DioDataHolder update(UUID holderId, DioDataHolder holder)
      throws ValidationListException, NotFoundException {
    /**
     * Rewrite holder id
     */
    holder.id(holderId);

    /**
     * Validate input data
     */
    validationService.validate(holder, MessageUtil
        .format(io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, holder));

    /**
     * Locate existing holder
     */
    DataHolderData holderData =
        holderRepository.findById(holderId).orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_ID, holderId)).build());

    /**
     * Map supplied data over the top
     */
    mapper.map(holder, holderData);
    DataHolderData savedDataHolder = holderRepository.save(holderData);
    LOG.debug(
        MessageUtil.format(io.biza.babelfish.spring.Messages.UPDATED_GENERIC_WITH_CONTENT, TYPE_NAME_DB, savedDataHolder));
    return mapper.map(savedDataHolder, DioDataHolder.class);
  }

  public DioDataHolder read(UUID holderId) throws NotFoundException {

    /**
     * Locate existing holder
     */
    DataHolderData holderData =
        holderRepository.findById(holderId).orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_ID, holderId)).build());

    return mapper.map(holderData, DioDataHolder.class);

  }

  public void delete(UUID holderId) throws NotFoundException {

    /**
     * Locate existing holder
     */
    DataHolderData holderData =
        holderRepository.findById(holderId).orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_ID, holderId)).build());

    /**
     * Now delete them
     */
    holderRepository.delete(holderData);

  }


}

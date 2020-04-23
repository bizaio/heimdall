package io.biza.heimdall.shared.component.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.babelfish.spring.service.common.OrikaMapperService;
import io.biza.babelfish.spring.service.common.ValidationService;
import io.biza.heimdall.shared.Messages;
import io.biza.babelfish.cdr.util.MessageUtil;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HolderBrandService {

  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  DataHolderBrandRepository holderBrandRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  OrikaMapperService mapper;

  public static final String TYPE_NAME_PAYLOAD = DioDataHolderBrand.class.getName();
  public static final String TYPE_NAME_DB = DataHolderBrandData.class.getName();


  public DioDataHolderBrand create(UUID holderId, DioDataHolderBrand holderBrand)
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
    validationService.validate(holderBrand, MessageUtil
        .format(io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, holderBrand));

    /**
     * Create Data Holder Brand Record
     */
    DataHolderBrandData dataHolderBrandData =
        mapper.map(holderBrand, DataHolderBrandData.class).dataHolder(holderData);
    
    DataHolderBrandData savedDataHolderBrand = holderBrandRepository.save(dataHolderBrandData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataHolderBrand));
    return mapper.map(savedDataHolderBrand, DioDataHolderBrand.class);
  }


  public <T> Page<T> list(Specification<DataHolderBrandData> specification,
      Pageable pageable, Class<T> clazz) {
	  
    if (specification == null) {
      specification = Specification.where(null);
    }

    Page<DataHolderBrandData> dataHolderBrandData;

    /**
     * List all hodlers
     */
    if (pageable != null) {
      dataHolderBrandData = holderBrandRepository.findAll(specification, pageable);
    } else {
      dataHolderBrandData =
          new PageImpl<DataHolderBrandData>(holderBrandRepository.findAll(specification));
    }

    /**
     * Reconstruct Page
     */
    Page<T> page = new PageImpl<T>(
        mapper.mapAsList(dataHolderBrandData.getContent(), clazz),
        dataHolderBrandData.getPageable(), dataHolderBrandData.getTotalElements());

    /**
     * Map as a list
     */
    return page;
  }

  public DioDataHolderBrand update(UUID holderId, UUID brandId, DioDataHolderBrand holderBrand)
      throws ValidationListException, NotFoundException {
    /**
     * Rewrite holder id
     */
    holderBrand.id(brandId);

    /**
     * Validate input data
     */
    validationService.validate(holderBrand, MessageUtil
        .format(io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, holderBrand));

    /**
     * Locate existing holder
     */
    DataHolderBrandData holderBrandData = holderBrandRepository
        .findByIdAndDataHolderId(brandId, holderId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_BRAND_ID, holderId, brandId))
            .build());

    /**
     * Map supplied data over the top
     */
    mapper.map(holderBrand, holderBrandData);
    DataHolderBrandData savedDataHolderBrand = holderBrandRepository.save(holderBrandData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.UPDATED_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedDataHolderBrand));
    return mapper.map(savedDataHolderBrand, DioDataHolderBrand.class);
  }

  public DioDataHolderBrand read(UUID holderId, UUID brandId) throws NotFoundException {

    /**
     * Locate existing holder
     */
    DataHolderBrandData holderBrandData = holderBrandRepository
        .findByIdAndDataHolderId(brandId, holderId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_BRAND_ID, holderId, brandId))
            .build());

    return mapper.map(holderBrandData, DioDataHolderBrand.class);

  }

  public void delete(UUID holderId, UUID brandId) throws NotFoundException {

    /**
     * Locate existing holder
     */
    DataHolderBrandData holderBrandData = holderBrandRepository
        .findByIdAndDataHolderId(brandId, holderId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_HOLDER_BRAND_ID, holderId, brandId))
            .build());

    /**
     * Now delete them
     */
    holderBrandRepository.delete(holderBrandData);

  }


}

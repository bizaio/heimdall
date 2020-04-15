package io.biza.heimdall.shared.component.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.payloads.JWTClaims;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.babelfish.spring.interfaces.OldJWKService;
import io.biza.babelfish.spring.service.ValidationService;
import io.biza.babelfish.spring.util.MessageUtil;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.Messages;
import io.biza.heimdall.shared.component.support.HeimdallMapper;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioSoftwareProduct;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SoftwareProductService {

  @Autowired
  SoftwareProductRepository softwareProductRepository;

  @Autowired
  DataRecipientBrandRepository brandRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  OldJWKService jwkService;

  @Autowired
  private HeimdallMapper mapper;

  public static final String TYPE_NAME_PAYLOAD = DioSoftwareProduct.class.getName();
  public static final String TYPE_NAME_DB = SoftwareProductData.class.getName();

  public DioSoftwareProduct create(UUID recipientId, UUID brandId,
      DioSoftwareProduct softwareProduct) throws ValidationListException, NotFoundException {

    /**
     * Locate existing data recipient brand
     */
    DataRecipientBrandData brandData = brandRepository
        .findByIdAndDataRecipientId(brandId, recipientId)
        .orElseThrow(() -> NotFoundException.builder().message(
            MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_ID, recipientId, brandId))
            .build());

    /**
     * Validate input data
     */
    validationService.validate(softwareProduct, MessageUtil.format(
        io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, softwareProduct));

    /**
     * Create Data Recipient Brand Record
     */
    SoftwareProductData softwareProductData =
        mapper.map(softwareProduct, SoftwareProductData.class);
    softwareProductData.id(UUID.randomUUID());
    softwareProductData.dataRecipientBrand(brandData);
    SoftwareProductData savedBrandSoftwareProduct =
        softwareProductRepository.save(softwareProductData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.CREATED_NEW_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        savedBrandSoftwareProduct));
    return mapper.map(savedBrandSoftwareProduct, DioSoftwareProduct.class);
  }

  public Page<DioSoftwareProduct> list(Specification<SoftwareProductData> specification,
      Pageable pageable) {

    if (specification == null) {
      specification = Specification.where(null);
    }

    Page<SoftwareProductData> data;

    /**
     * List all hodlers
     */
    if (pageable != null) {
      data = softwareProductRepository.findAll(specification, pageable);
    } else {
      data = new PageImpl<SoftwareProductData>(softwareProductRepository.findAll(specification));
    }

    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.LIST_ALL_GENERIC_AND_RECEIVED, TYPE_NAME_DB, data));

    /**
     * Reconstruct Page
     */
    Page<DioSoftwareProduct> page = new PageImpl<DioSoftwareProduct>(
        mapper.mapAsList(data.getContent(), DioSoftwareProduct.class), data.getPageable(),
        data.getTotalElements());

    /**
     * Map as a list
     */
    return page;
  }

  public DioSoftwareProduct update(UUID recipientId, UUID brandId, UUID productId,
      DioSoftwareProduct softwareProduct) throws ValidationListException, NotFoundException {
    /**
     * Rewrite recipient id
     */
    softwareProduct.id(productId);

    /**
     * Validate input data
     */
    validationService.validate(softwareProduct, MessageUtil.format(
        io.biza.babelfish.spring.Messages.UNABLE_TO_VALIDATE_GENERIC_WITH_CONTENT, TYPE_NAME_PAYLOAD, softwareProduct));

    /**
     * Locate existing recipient
     */
    SoftwareProductData softwareProductData = softwareProductRepository
        .findByIdAndDataRecipientBrandIdAndDataRecipientBrandDataRecipientId(productId, brandId,
            recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_PRODUCT_ID,
                recipientId, brandId, productId))
            .build());

    /**
     * Map supplied data over the top
     */
    mapper.map(softwareProduct, softwareProductData);
    SoftwareProductData savedSoftwareProduct = softwareProductRepository.save(softwareProductData);
    LOG.debug(MessageUtil.format(io.biza.babelfish.spring.Messages.UPDATED_GENERIC_WITH_CONTENT, TYPE_NAME_DB,
        softwareProductData));
    return mapper.map(savedSoftwareProduct, DioSoftwareProduct.class);
  }

  public DioSoftwareProduct read(UUID recipientId, UUID brandId, UUID productId)
      throws NotFoundException {

    /**
     * Locate existing recipient
     */
    SoftwareProductData recipientData = softwareProductRepository
        .findByIdAndDataRecipientBrandIdAndDataRecipientBrandDataRecipientId(productId, brandId,
            recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_PRODUCT_ID,
                recipientId, brandId, productId))
            .build());

    return mapper.map(recipientData, DioSoftwareProduct.class);

  }

  public String getSoftwareStatementAssertion(UUID brandId, UUID productId)
      throws SigningOperationException, NotFoundException {
    SoftwareProductData softwareProduct =
        softwareProductRepository.findByIdAndDataRecipientBrandId(productId, brandId)
            .orElseThrow(() -> NotFoundException
                .builder().message(MessageUtil
                    .format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_PRODUCT_ID, brandId, productId))
                .build());

    /**
     * Build claim set
     */
    Map<String, Object> additionalClaims = new HashMap<String, Object>();
    additionalClaims.put("org_id", softwareProduct.dataRecipientBrand().id().toString());
    additionalClaims.put("org_name", softwareProduct.dataRecipientBrand().brandName().toString());
    additionalClaims.put("client_name", softwareProduct.name().toString());
    additionalClaims.put("client_description", softwareProduct.description().toString());
    additionalClaims.put("client_uri", softwareProduct.uri().toString());
    additionalClaims.put("redirect_uris", softwareProduct.redirectUris().stream()
        .map(uri -> uri.toString()).collect(Collectors.toList()));
    additionalClaims.put("logo_uri", softwareProduct.logoUri().toString());
    additionalClaims.put("tos_uri", softwareProduct.tosUri().toString());
    additionalClaims.put("policy_uri", softwareProduct.policyUri().toString());
    additionalClaims.put("jwks_uri", softwareProduct.jwksUri().toString());
    additionalClaims.put("revocation_uri", softwareProduct.revocationUri().toString());
    additionalClaims.put("software_id", softwareProduct.id().toString());
    additionalClaims.put("software_roles", softwareProduct.softwareRole().toString());


    JWTClaims ssaClaims = JWTClaims.builder().issuer(Constants.REGISTER_ISSUER)
        .expiry(OffsetDateTime.now().plusHours(Constants.REGISTER_SSA_LENGTH_HOURS))
        .scope(
            softwareProduct.scopes().stream().map(CDRScope::toString).collect(Collectors.toList()))
        .issuedAt(OffsetDateTime.now()).jwtIdByUUID(UUID.randomUUID())
        .additionalClaims(additionalClaims).build();


    return jwkService.sign(ssaClaims, JWSSigningAlgorithmType.PS256);
  }

  public void delete(UUID recipientId, UUID brandId, UUID productId) throws NotFoundException {

    /**
     * Locate existing recipient
     */
    SoftwareProductData softwareProductData = softwareProductRepository
        .findByIdAndDataRecipientBrandIdAndDataRecipientBrandDataRecipientId(productId, brandId,
            recipientId)
        .orElseThrow(() -> NotFoundException.builder()
            .message(MessageUtil.format(Messages.UNABLE_TO_FIND_RECIPIENT_BRAND_PRODUCT_ID,
                recipientId, brandId, productId))
            .build());
    /**
     * Now delete them
     */
    softwareProductRepository.delete(softwareProductData);

  }


}

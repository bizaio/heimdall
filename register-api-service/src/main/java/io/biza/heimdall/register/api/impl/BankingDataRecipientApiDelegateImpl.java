package io.biza.heimdall.register.api.impl;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;
import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientStatus;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipient;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductStatus;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusList;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientList;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusList;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.payloads.JWTClaims;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.interfaces.JWKService;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {

  @Autowired
  DataRecipientRepository dataRecipientRepository;

  @Autowired
  SoftwareProductRepository productRepository;

  @Autowired
  JWKService jwkService;

  @Autowired
  HeimdallMapper mapper;

  @Override
  public ResponseEntity<ResponseRegisterDataRecipientList> getBankingDataRecipients() {
    List<DataRecipientData> dataRecipientData = dataRecipientRepository.findAll();

    ResponseRegisterDataRecipientList listResponse = ResponseRegisterDataRecipientList.builder()
        .data(mapper.mapAsList(dataRecipientData, RegisterDataRecipient.class)).build();
    LOG.debug("List recipients response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<DataRecipientsStatusList> getBankingDataRecipientStatuses() {
    List<DataRecipientData> dataRecipientData = dataRecipientRepository.findAll();

    DataRecipientsStatusList listResponse = DataRecipientsStatusList.builder()
        .dataRecipients(mapper.mapAsList(dataRecipientData, DataRecipientStatus.class)).build();
    LOG.debug("List recipient statuses response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<RawJson> getSoftwareStatementAssertion(UUID brandId, UUID productId) {
    Optional<SoftwareProductData> softwareProductOptional =
        productRepository.findByIdAndDataRecipientBrandId(productId, brandId);

    if (!softwareProductOptional.isPresent()) {
      LOG.warn("Attempted to produce SSA for non existent brand {} and product of {}", brandId,
          productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    SoftwareProductData softwareProduct = softwareProductOptional.get();

    /**
     * Build claim set
     */
    Map<String, Object> additionalClaims = new HashMap<String, Object>();
    additionalClaims.put("org_id", softwareProduct.dataRecipientBrand().id().toString());
    additionalClaims.put("org_name", softwareProduct.dataRecipientBrand().brandName().toString());
    additionalClaims.put("client_name", softwareProduct.name().toString());
    additionalClaims.put("client_description", softwareProduct.description().toString());
    additionalClaims.put("client_uri", softwareProduct.uri().toString());
    additionalClaims.put("redirect_uris",
        softwareProduct.redirectUris().stream().map(uri -> uri.toString()).collect(Collectors.toList()));
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


    try {
      return ResponseEntity.ok(RawJson.from(jwkService.sign(ssaClaims, JWSSigningAlgorithmType.PS256)));
    } catch (SigningOperationException e) {
      LOG.error(
          "Encountered JOSE Signing Error despite having a Register key, something went wrong in crypto land", e);
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

  @Override
  public ResponseEntity<SoftwareProductsStatusList> getSoftwareProductStatuses() {
    List<SoftwareProductData> softwareProductData = productRepository.findAll();

    SoftwareProductsStatusList listResponse = SoftwareProductsStatusList.builder()
        .softwareProducts(mapper.mapAsList(softwareProductData, SoftwareProductStatus.class))
        .build();
    LOG.debug("List software product statuses response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

}

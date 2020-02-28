package io.biza.heimdall.register.api.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.payload.enumerations.JWKStatus;
import io.biza.heimdall.payload.enumerations.RegisterScope;
import io.biza.heimdall.payload.holder.RegisterDataHolderBrand;
import io.biza.heimdall.payload.recipient.DataRecipientStatus;
import io.biza.heimdall.payload.recipient.RegisterDataRecipient;
import io.biza.heimdall.payload.recipient.SoftwareProductStatus;
import io.biza.heimdall.payload.registration.SoftwareStatementAssertion;
import io.biza.heimdall.payload.responses.DataRecipientsStatusList;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;
import io.biza.heimdall.payload.responses.ResponseRegisterDataRecipientList;
import io.biza.heimdall.payload.responses.SoftwareProductsStatusList;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.RegisterJWKData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterJWKRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.persistence.specifications.DataHolderBrandSpecifications;
import io.biza.heimdall.shared.util.RawJson;
import io.biza.heimdall.shared.util.RegisterContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {

  @Autowired
  DataRecipientBrandRepository dataRecipientBrandRepository;

  @Autowired
  RegisterJWKRepository jwkRepository;

  @Autowired
  SoftwareProductRepository productRepository;

  @Autowired
  HeimdallMapper mapper;

  @Override
  public ResponseEntity<ResponseRegisterDataRecipientList> getBankingDataRecipients() {
    List<DataRecipientBrandData> dataRecipientData = dataRecipientBrandRepository.findAll();

    ResponseRegisterDataRecipientList listResponse = ResponseRegisterDataRecipientList.builder()
        .data(mapper.mapAsList(dataRecipientData, RegisterDataRecipient.class)).build();
    LOG.debug("List recipients response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<DataRecipientsStatusList> getBankingDataRecipientStatuses() {
    List<DataRecipientBrandData> dataRecipientData = dataRecipientBrandRepository.findAll();

    DataRecipientsStatusList listResponse = DataRecipientsStatusList.builder()
        .dataRecipients(mapper.mapAsList(dataRecipientData, DataRecipientStatus.class)).build();
    LOG.debug("List recipient statuses response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<RawJson> getSoftwareStatementAssertion(UUID brandId,
      UUID productId) {

    JsonWebKey registerJwk;

    /**
     * If jwks isn't initialised we can't do anything
     */
    List<RegisterJWKData> jwkList = jwkRepository.findByStatusIn(List.of(JWKStatus.ACTIVE));
    if (jwkList == null || jwkList.size() < 0) {
      LOG.error(
          "Attempted to retrieve an SSA when there are no initialised and active JWKs to sign it with");
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    } else {
      Random randomKeyIndex = new Random();
      RegisterJWKData jwkData = jwkList.get(randomKeyIndex.nextInt(jwkList.size()));
      try {
        registerJwk = JsonWebKey.Factory.newJwk(jwkData.jwk());

      } catch (JoseException e) {
        LOG.error("Encountered a JOSE Exception while loading register json web key {}", e.toString());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
      }
    }

    Optional<SoftwareProductData> softwareProductOptional =
        productRepository.findByIdAndDataRecipientBrandId(productId, brandId);

    if (softwareProductOptional.isPresent()) {

      SoftwareProductData softwareProduct = softwareProductOptional.get();

      /**
       * Build claim set
       */
      JwtClaims ssaClaims = new JwtClaims();
      ssaClaims.setIssuer(Constants.REGISTER_ISSUER);
      ssaClaims.setExpirationTimeMinutesInTheFuture(60 * Constants.REGISTER_SSA_LENGTH_HOURS);
      ssaClaims.setClaim("org_id", softwareProduct.dataRecipientBrand().id().toString());
      ssaClaims.setClaim("org_name", softwareProduct.dataRecipientBrand().brandName());
      ssaClaims.setClaim("client_name", softwareProduct.name());
      ssaClaims.setClaim("client_description", softwareProduct.description());
      ssaClaims.setClaim("client_uri", softwareProduct.uri());
      ssaClaims.setClaim("redirect_uris",
          softwareProduct.redirectUris().stream().collect(Collectors.toList()));
      ssaClaims.setIssuedAtToNow();
      ssaClaims.setGeneratedJwtId();
      ssaClaims.setClaim("logo_uri", softwareProduct.logoUri());
      ssaClaims.setClaim("tos_uri", softwareProduct.tosUri());
      ssaClaims.setClaim("policy_uri", softwareProduct.policyUri());
      ssaClaims.setClaim("jwks_uri", softwareProduct.jwksUri());
      ssaClaims.setClaim("revocation_uri", softwareProduct.revocationUri());
      ssaClaims.setClaim("software_id", softwareProduct.id().toString());
      ssaClaims.setClaim("software_roles", softwareProduct.softwareRole());
      ssaClaims.setClaim("scope", softwareProduct.scopes().stream().map(RegisterScope::toString)
          .collect(Collectors.joining(" ")));
      
      /**
       * Perform signing
       */
      JsonWebSignature jws = new JsonWebSignature();
      jws.setPayload(ssaClaims.toJson());
      jws.setKey(registerJwk.getKey());
      jws.setKeyIdHeaderValue(registerJwk.getKeyId());
      jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);
      try {
        String jwsResult = jws.getCompactSerialization();
        return ResponseEntity.ok(RawJson.from(jwsResult));
      } catch (JoseException e) {
        LOG.error("Encountered JOSE Signing Error despite having a Register key, something went wrong in crypto land: {}", e.toString());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
      }
      
    } else {
      LOG.warn("Attempted to produce SSA for non existent brand {} and product of {}", brandId,
          productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

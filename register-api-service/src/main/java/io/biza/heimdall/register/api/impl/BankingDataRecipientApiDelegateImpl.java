package io.biza.heimdall.register.api.impl;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.babelfish.cdr.enumerations.register.RegisterScope;
import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientStatus;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipient;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductStatus;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusList;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientList;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusList;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.util.RawJson;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {

  @Autowired
  DataRecipientRepository dataRecipientRepository;

  @Autowired
  RegisterAuthorityJWKRepository jwkRepository;

  @Autowired
  SoftwareProductRepository productRepository;

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

    PublicJsonWebKey registerJwk;

    /**
     * If jwks isn't initialised we can't do anything
     */
    RegisterAuthorityJWKData jwkData = jwkRepository.findFirstByStatusIn(List.of(JWKStatus.ACTIVE));
    if (jwkData == null) {
      LOG.error(
          "Attempted to retrieve an SSA when there are no initialised and active JWKs to sign it with");
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    } else {


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
        try {
          JsonWebSignature jws = new JsonWebSignature();
          jws.setPayload(ssaClaims.toJson());
          jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);
          KeyFactory keyFactory = KeyFactory.getInstance(jwkData.javaFactory());
          PKCS8EncodedKeySpec privateKeySpec =
              new PKCS8EncodedKeySpec(Base64.getDecoder().decode(jwkData.privateKey()));
          PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

          jws.setKey(privateKey);
          jws.setKeyIdHeaderValue(jwkData.id().toString());

          String jwsResult = jws.getCompactSerialization();
          return ResponseEntity.ok(RawJson.from(jwsResult));
        } catch (JoseException e) {
          LOG.error(
              "Encountered JOSE Signing Error despite having a Register key, something went wrong in crypto land: {}",
              e.toString());
          return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (NoSuchAlgorithmException e) {
          LOG.error("Encountered an algorithm in the database I no longer know how to process? {}",
              e.toString());
          return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (InvalidKeySpecException e) {
          LOG.error("Encountered error during private key import into key specification: {}",
              e.toString());
          return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

      } else {
        LOG.warn("Attempted to produce SSA for non existent brand {} and product of {}", brandId,
            productId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
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

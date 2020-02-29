package io.biza.heimdall.register.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import io.biza.heimdall.payload.enumerations.CertificateStatus;
import io.biza.heimdall.payload.enumerations.JWKStatus;
import io.biza.heimdall.payload.holder.RegisterDataHolderBrand;
import io.biza.heimdall.payload.recipient.DataRecipientStatus;
import io.biza.heimdall.payload.recipient.RegisterDataRecipient;
import io.biza.heimdall.payload.recipient.SoftwareProductStatus;
import io.biza.heimdall.payload.registration.SoftwareStatementAssertion;
import io.biza.heimdall.payload.responses.DataRecipientsStatusList;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;
import io.biza.heimdall.payload.responses.ResponseRegisterDataRecipientList;
import io.biza.heimdall.payload.responses.SoftwareProductsStatusList;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.register.api.delegate.RegisterApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.payloads.dio.DioRegisterJWK;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.RegisterCertificateAuthorityData;
import io.biza.heimdall.shared.persistence.model.RegisterJWKData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterCertificateAuthorityRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterJWKRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.persistence.specifications.DataHolderBrandSpecifications;
import io.biza.heimdall.shared.util.RawJson;
import io.biza.heimdall.shared.util.RegisterContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class RegisterApiDelegateImpl implements RegisterApiDelegate {

  @Autowired
  RegisterJWKRepository jwkRepository;
  
  @Autowired
  RegisterCertificateAuthorityRepository caRepository;

  @Override
  public ResponseEntity<String> getJwks() {
    
    List<RegisterJWKData> registerData = jwkRepository.findByStatusIn(List.of(JWKStatus.ACTIVE));
    
    JsonWebKeySet jsonWebKeySet = new JsonWebKeySet();
    registerData.forEach(jwk -> {
      try {
        jsonWebKeySet.addJsonWebKey(JsonWebKey.Factory.newJwk(jwk.jwk()));
      } catch (JoseException e) {
        LOG.error("Received error while parsing JWK from Database");
      }
    });
    
    return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(jsonWebKeySet.toJson(OutputControlLevel.PUBLIC_ONLY));
    
  }
  
  @Override
  public ResponseEntity<String> getCertificateAuthority() {
    List<RegisterCertificateAuthorityData> caData = caRepository.findByStatusIn(List.of(CertificateStatus.ACTIVE));
    
    if(caData != null && caData.size() > 0) {
      String publicCertificate = new StringBuilder().append("-----BEGIN CERTIFICATE-----\n").append(caData.get(0).publicKey().replaceAll(".{80}(?=.)", "$0\n")).append("\n-----END CERTIFICATE-----\n").toString();
      return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(publicCertificate);
    } else {
      LOG.error("Unable to provide CA public key when CA is not initialised");
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    
  }

}

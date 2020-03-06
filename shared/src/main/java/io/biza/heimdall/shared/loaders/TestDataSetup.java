/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *******************************************************************************/
package io.biza.heimdall.shared.loaders;

import java.math.BigInteger;
import java.net.URI;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.babelfish.cdr.enumerations.register.CDRVersionType;
import io.biza.babelfish.cdr.enumerations.register.CertificateStatus;
import io.biza.babelfish.cdr.enumerations.register.DataHolderStatusType;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientBrandStatusType;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.babelfish.cdr.enumerations.register.RegisterAuthType;
import io.biza.babelfish.cdr.enumerations.register.RegisterScope;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.heimdall.shared.enumerations.DioClientCredentialType;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandAuthData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandEndpointData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.LegalEntityRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Profile({"test", "dev"})
public class TestDataSetup implements ApplicationListener<ApplicationReadyEvent> {
  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  DataHolderBrandRepository holderBrandRepository;

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  DataRecipientBrandRepository recipientBrandRepository;

  @Autowired
  SoftwareProductRepository productRepository;

  @Autowired
  LegalEntityRepository legalRepository;

  @Autowired
  ClientRepository clientRepository;


  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

    if (!clientRepository.existsById(UUID.fromString(TestDataConstants.HOLDER_CLIENT_ID))) {
      LOG.warn("Loading Test Data Holder information");
      DataHolderData holder = holderRepository.save(DataHolderData.builder()
          .industry(IndustryType.BANKING)
          .legalEntity(LegalEntityData.builder().legalName(TestDataConstants.HOLDER_LEGAL_NAME)
              .organisationType(CommonOrganisationType.COMPANY).build())
          .dataHolderBrands(Set.of(DataHolderBrandData.builder()
              .authDetails(Set.of(DataHolderBrandAuthData.builder()
                  .authType(RegisterAuthType.HYBRIDFLOW_JWKS)
                  .jwksEndpoint(URI.create("https://localhost:" + TestDataConstants.HOLDER_AUTH_PORT
                      + TestDataConstants.HOLDER_AUTH_JWKS_PATH))
                  .build()))
              .brandName(TestDataConstants.HOLDER_BRAND_NAME).lastUpdated(OffsetDateTime.now())
              .logoUri(TestDataConstants.HOLDER_BRAND_LOGO_URI).status(DataHolderStatusType.ACTIVE)
              .endpointDetail(DataHolderBrandEndpointData.builder()
                  .infosecBaseUri(TestDataConstants.HOLDER_BRAND_INFOSEC_URI)
                  .resourceBaseUri(TestDataConstants.HOLDER_BRAND_RESOURCE_URI)
                  .publicBaseUri(TestDataConstants.HOLDER_BRAND_PUBLIC_URI)
                  .version(CDRVersionType.V1).websiteUri(TestDataConstants.HOLDER_BRAND_WEBSITE)
                  .build())
              .build()))
          .name(TestDataConstants.HOLDER_NAME).build());

      LOG.info("Loaded holder as {}", holder);

      ClientData holderClient = clientRepository
          .save(ClientData.builder().id(UUID.fromString(TestDataConstants.HOLDER_CLIENT_ID))
              .credentialType(DioClientCredentialType.CLIENT_CREDENTIALS_SECRET)
              .clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET).build().dataHolder(holder));

      LOG.info("Loaded holder client as {}", holderClient);

    }

    if (!clientRepository.existsById(UUID.fromString(TestDataConstants.RECIPIENT_CLIENT_ID))) {
      LOG.warn("Loading Test Data Recipient information");


      DataRecipientData recipient = recipientRepository.save(DataRecipientData.builder()
          .industry(IndustryType.BANKING)
          .legalEntity(LegalEntityData.builder().legalName(TestDataConstants.RECIPIENT_LEGAL_NAME)
              .organisationType(CommonOrganisationType.COMPANY).build())
          .logoUri(TestDataConstants.RECIPIENT_LOGO_URI)
          .status(DataRecipientStatusType.ACTIVE).build());

      LOG.info("Loaded recipient as {}", recipient);


      DataRecipientBrandData brand = recipientBrandRepository
          .save(DataRecipientBrandData.builder().brandName(TestDataConstants.RECIPIENT_NAME)
              .logoUri(TestDataConstants.RECIPIENT_BRAND_LOGO_URI)
              .status(DataRecipientBrandStatusType.ACTIVE).build().dataRecipient(recipient));

      SoftwareProductData product = productRepository.save(SoftwareProductData.builder()
          .jwksUri(URI.create("http://localhost:" + TestDataConstants.RECIPIENT_PORT
              + TestDataConstants.RECIPIENT_JWKS_PATH))
          .name(TestDataConstants.RECIPIENT_PRODUCT_NAME)
          .uri(TestDataConstants.RECIPIENT_PRODUCT_WEBSITE)
          .description(TestDataConstants.RECIPIENT_PRODUCT_DESCRIPTION)
          .logoUri(TestDataConstants.RECIPIENT_PRODUCT_LOGO_URI)
          .tosUri(TestDataConstants.RECIPIENT_PRODUCT_TOS)
          .policyUri(TestDataConstants.RECIPIENT_PRODUCT_POLICY)
          .revocationUri(TestDataConstants.RECIPIENT_PRODUCT_REVOCATION_URI).scopes(Set
              .of(RegisterScope.BANK_ACCOUNTS_BASIC_READ, RegisterScope.BANK_ACCOUNTS_DETAIL_READ))
          .redirectUris(TestDataConstants.RECIPIENT_PRODUCT_REDIRECT_URI)
          .build().dataRecipientBrand(brand));

      LOG.info("Saved: {}", product);

      ClientData recipientClient =
          ClientData.builder().credentialType(DioClientCredentialType.CLIENT_CREDENTIALS_ASSERTION)
              .id(UUID.fromString(TestDataConstants.RECIPIENT_CLIENT_ID)).build()
              .softwareProduct(product);

      LOG.info("Saving {}", recipientClient);

      clientRepository.save(recipientClient);

    }
  }
}

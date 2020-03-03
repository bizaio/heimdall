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
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.biza.babelfish.cdr.enumerations.register.CertificateStatus;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientBrandStatusType;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Profile("test")
public class TestDataSetup {
  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  DataRecipientBrandRepository recipientBrandRepository;

  @Autowired
  SoftwareProductRepository productRepository;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  public TestDataSetup(DataHolderRepository holderRepository,
      ClientRepository holderClientRepository) {
    this.holderRepository = holderRepository;
    this.clientRepository = holderClientRepository;
    loadTestData();
  }

  private void loadTestData() {

    LOG.warn("Loading Test Data Holder information");
    DataHolderData holder =
        holderRepository.save(DataHolderData.builder().industry(IndustryType.BANKING)
            .legalEntity(
                LegalEntityData.builder().legalName(TestDataConstants.HOLDER_LEGAL_NAME).build())
            .name(TestDataConstants.HOLDER_NAME).build());
    ClientData holderClient =
        clientRepository.save(ClientData.builder().id(UUID.fromString(TestDataConstants.CLIENT_ID))
            .clientSecret(TestDataConstants.CLIENT_SECRET).build().dataHolder(holder));

    LOG.warn("Loading Test Data Recipient information");
    DataRecipientData recipient =
        recipientRepository.save(DataRecipientData.builder().industry(IndustryType.BANKING)
            .legalEntity(
                LegalEntityData.builder().legalName(TestDataConstants.RECIPIENT_LEGAL_NAME).build())
            .status(DataRecipientStatusType.ACTIVE).build());

    DataRecipientBrandData brand = recipientBrandRepository
        .save(DataRecipientBrandData.builder().brandName(TestDataConstants.RECIPIENT_NAME)
            .status(DataRecipientBrandStatusType.ACTIVE).build().dataRecipient(recipient));

    SoftwareProductData product =
        productRepository
            .save(
                SoftwareProductData.builder()
                    .jwksUri(URI.create("http://localhost:" + TestDataConstants.RECIPIENT_PORT
                        + TestDataConstants.RECIPIENT_JWKS_PATH))
                    .build().dataRecipientBrand(brand));

    ClientData recipientClient = clientRepository.save(ClientData.builder()
        .id(UUID.fromString(TestDataConstants.CLIENT_ID)).build().softwareProduct(product));
  }
}

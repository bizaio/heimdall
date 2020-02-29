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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.BigInteger;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.X509CertPairParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.biza.babelfish.cdr.support.customtypes.ApcaNumberType;
import io.biza.heimdall.payload.enumerations.CertificateStatus;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.persistence.model.RegisterCertificateAuthorityData;
import io.biza.heimdall.shared.persistence.repository.RegisterCertificateAuthorityRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(1)
public class CertificateAuthorityGenerator {
  private RegisterCertificateAuthorityRepository caRepository;

  @Autowired
  public CertificateAuthorityGenerator(RegisterCertificateAuthorityRepository caRepository) {
    this.caRepository = caRepository;
    createCertificateAuthority();
  }

  private void createCertificateAuthority() {
    List<RegisterCertificateAuthorityData> certificates =
        caRepository.findByStatusIn(List.of(CertificateStatus.ACTIVE));

    if (certificates != null && certificates.size() > 0) {
      LOG.info("Certificate Authority already initialised, skipping CA generation");
      LOG.info("PEM of Certificate Authority is output to STDOUT below");
      System.out.println("-----BEGIN RSA PRIVATE KEY-----");
      System.out.println(certificates.get(0).privateKey().replaceAll(".{80}(?=.)", "$0\n"));
      System.out.println("-----END RSA PRIVATE KEY-----\n-----BEGIN CERTIFICATE-----");
      System.out.println(certificates.get(0).publicKey().replaceAll(".{80}(?=.)", "$0\n"));
      System.out.println("-----END CERTIFICATE-----");
      return;
    }

    try {
      /**
       * Private key generation
       */
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constants.CA_ALGORITHM);
      keyGen.initialize(Constants.CA_KEY_SIZE);
      KeyPair keyPair = keyGen.generateKeyPair();

      /**
       * Bouncycastle Registration
       */
      Provider bcProvider = new BouncyCastleProvider();
      Security.addProvider(bcProvider);

      /**
       * Certificate Start time
       */
      long timeNow = System.currentTimeMillis();
      Date startDate = new Date(timeNow);

      /**
       * Expiration time
       */
      Calendar expiryTime = Calendar.getInstance();
      expiryTime.setTime(startDate);
      expiryTime.add(Calendar.YEAR, Constants.CA_VALIDITY_YEARS);

      SubjectPublicKeyInfo subjectPublicKeyInfo =
          SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

      /**
       * Set it all up and declare us a CA
       */

      X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
          new X500Name(Constants.CA_DN), new BigInteger(Long.toString(timeNow)), startDate,
          expiryTime.getTime(), new X500Name(Constants.CA_DN), subjectPublicKeyInfo);
      certificateBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true,
          new BasicConstraints(true));
      ContentSigner contentSigner = new JcaContentSignerBuilder(Constants.CA_SIGNING_ALGORITHM)
          .setProvider(bcProvider).build(keyPair.getPrivate());

      /**
       * Show time baby!
       */
      X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);
      X509Certificate selfSignedCert =
          new JcaX509CertificateConverter().getCertificate(certificateHolder);

      /**
       * (Insecurely!!!!) Save it to the database
       */
      RegisterCertificateAuthorityData caCertData = RegisterCertificateAuthorityData.builder()
          .privateKey(new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded())))
          .publicKey(new String(Base64.getEncoder().encode(selfSignedCert.getEncoded())))
          .status(CertificateStatus.ACTIVE).build();
      RegisterCertificateAuthorityData savedCaCertData = caRepository.save(caCertData);

      LOG.warn("Certificate authority initialisation has been completed!");
      LOG.info("PEM of Certificate Authority is output to STDOUT below");
      System.out.println("-----BEGIN RSA PRIVATE KEY-----");
      System.out.println(savedCaCertData.privateKey().replaceAll(".{80}(?=.)", "$0\n"));
      System.out.println("-----END RSA PRIVATE KEY-----\n-----BEGIN CERTIFICATE-----");
      System.out.println(savedCaCertData.publicKey().replaceAll(".{80}(?=.)", "$0\n"));
      System.out.println("-----END CERTIFICATE-----");
      
    } catch (NoSuchAlgorithmException e) {
      LOG.error("Invalid algorithm of {} specified, cannot initialise certificate authority!",
          Constants.CA_ALGORITHM);
    } catch (InvalidParameterException e) {
      LOG.error("Invalid key size of {} specified, cannot initialise certificate authority!",
          Constants.CA_KEY_SIZE);
    } catch (OperatorCreationException e) {
      LOG.error("Operator error detected, spewing out stack trace");
      e.printStackTrace();
    } catch (CertIOException e) {
      LOG.error("Encountered certificate io error, cannot initialise certificate authority: {}",
          e.getMessage());
    } catch (CertificateException e) {
      LOG.error(
          "Encountered certificate conversion exception, cannot initialise certificate authority: {}",
          e.getMessage());
    }
  }
}

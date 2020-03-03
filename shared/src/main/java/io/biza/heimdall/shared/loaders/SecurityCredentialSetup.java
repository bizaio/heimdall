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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.biza.babelfish.cdr.enumerations.register.CertificateStatus;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(1)
public class SecurityCredentialSetup {
  private RegisterAuthorityTLSRepository caRepository;
  private RegisterAuthorityJWKRepository jwkRepository;


  @Autowired
  public SecurityCredentialSetup(RegisterAuthorityTLSRepository caRepository,
      RegisterAuthorityJWKRepository jwkRepository) {
    this.caRepository = caRepository;
    this.jwkRepository = jwkRepository;
    createRegisterJwk();
    createCertificateAuthority();
  }

  private void createRegisterJwk() {
    RegisterAuthorityJWKData jwk = jwkRepository.findFirstByStatusIn(List.of(JWKStatus.ACTIVE));

    if (jwk != null) {
      LOG.info("JWKS Authority already initialised, skipping JWK generation");
      LOG.info("JWK key identifier is {}", jwk.id());
      LOG.debug("JWK Authority details is output below");
      LOG.debug("\n\n-----BEGIN PRIVATE KEY-----\n" + jwk.privateKey()
          + "\n-----END PRIVATE KEY-----\n-----BEGIN PUBLIC KEY-----\n" + jwk.publicKey()
          + "\n-----END PUBLIC KEY-----\n\n");
      return;
    }

    /**
     * Generate a new keypair
     */
    KeyPairGenerator keyGen;
    try {
      keyGen = KeyPairGenerator.getInstance(Constants.JAVA_ALGORITHM);
      keyGen.initialize(2048);
      KeyPair keyPair = keyGen.generateKeyPair();

      /**
       * Setup JWK Data
       */


      RegisterAuthorityJWKData jwkData = jwkRepository.save(RegisterAuthorityJWKData.builder()
          .privateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
          .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
          .javaFactory(Constants.JAVA_ALGORITHM).joseAlgorithm(Constants.JOSE4J_ALGORITHM).status(JWKStatus.ACTIVE)
          .build());

      LOG.warn("JWKS Authority initialisation has been completed!");
      LOG.debug("Public key of JWKS output below");
      LOG.debug("-----BEGIN PUBLIC KEY-----\n" + jwkData.publicKey()
          + "\n-----END PUBLIC KEY-----\n\n");
    } catch (NoSuchAlgorithmException e) {
      LOG.error("Invalid algorithm of {} specified, cannot proceed", Constants.JAVA_ALGORITHM);
    }

  }

  private void createCertificateAuthority() {
    RegisterAuthorityTLSData caCertificate =
        caRepository.findFirstByStatusIn(List.of(CertificateStatus.ACTIVE));

    if (caCertificate != null) {
      LOG.info("Certificate Authority already initialised, skipping CA generation");
      LOG.debug("Public Certificate of Certificate Authority is output below");
      LOG.debug("\n-----BEGIN CERTIFICATE-----\n"
          + caCertificate.publicKey().replaceAll(".{80}(?=.)", "$0\n")
          + "\n-----END CERTIFICATE-----\n\n");
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
      RegisterAuthorityTLSData caCertData = RegisterAuthorityTLSData.builder()
          .privateKey(new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded())))
          .publicKey(new String(Base64.getEncoder().encode(selfSignedCert.getEncoded())))
          .status(CertificateStatus.ACTIVE).build();
      RegisterAuthorityTLSData savedCaCertData = caRepository.save(caCertData);

      LOG.warn("Certificate authority initialisation has been completed!");
      LOG.debug("PEM of Certificate Authority is output as follows");
      LOG.debug("\n\n-----BEGIN RSA PRIVATE KEY-----\n"
          + savedCaCertData.privateKey().replaceAll(".{80}(?=.)", "$0\n")
          + "\n-----END RSA PRIVATE KEY-----\n-----BEGIN CERTIFICATE-----\n"
          + savedCaCertData.publicKey().replaceAll(".{80}(?=.)", "$0\n")
          + "\n-----END CERTIFICATE-----\n\n");

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

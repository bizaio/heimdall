package io.biza.heimdall.admin.api.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import io.biza.heimdall.shared.Constants;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.enumerations.register.CertificateStatus;
import io.biza.babelfish.cdr.enumerations.register.CertificateType;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.heimdall.admin.api.delegate.RegisterAdministrationApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.payloads.dio.DioRegisterJWK;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import io.biza.heimdall.shared.payloads.requests.dio.RequestJwkCreate;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class RegisterAdministrationApiDelegateImpl implements RegisterAdministrationApiDelegate {

  @Autowired
  RegisterAuthorityJWKRepository jwkRepository;

  @Autowired
  RegisterAuthorityTLSRepository caRepository;

  @Autowired
  private HeimdallMapper mapper;

  public final String JAVA_ALGORITHM = "RSASSA-PSS";
  public final String JOSE4J_ALGORITHM = AlgorithmIdentifiers.RSA_PSS_USING_SHA256;

  @Override
  public ResponseEntity<DioRegisterJWK> createJwk(RequestJwkCreate jwkRequest)
      throws JoseException {

    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(JAVA_ALGORITHM);
      keyGen.initialize(2048);
      KeyPair keyPair = keyGen.generateKeyPair();

      /**
       * Setup JWK Data
       */
      RegisterAuthorityJWKData jwkData = jwkRepository.save(RegisterAuthorityJWKData.builder()
          .privateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
          .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
          .javaFactory(JAVA_ALGORITHM).joseAlgorithm(JOSE4J_ALGORITHM).status(JWKStatus.ACTIVE)
          .build());

      return ResponseEntity.ok(mapper.map(jwkData, DioRegisterJWK.class));

    } catch (NoSuchAlgorithmException e) {
      LOG.error("Invalid algorithm of {} specified, cannot proceed", JAVA_ALGORITHM);
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

  @Override
  public ResponseEntity<String> signCertificate(RequestCACertificateSign createRequest) {
    RegisterAuthorityTLSData caData =
        caRepository.findFirstByStatusIn(List.of(CertificateStatus.ACTIVE));

    /**
     * Bouncycastle Registration
     */
    Provider bcProvider = new BouncyCastleProvider();
    Security.addProvider(bcProvider);

    if (caData != null) {

      PKCS8EncodedKeySpec privateKeySpec =
          new PKCS8EncodedKeySpec(Base64.getDecoder().decode(caData.privateKey()));
      try {
        PrivateKey caPrivateKey =
            KeyFactory.getInstance(Constants.CA_ALGORITHM).generatePrivate(privateKeySpec);

        Certificate caCertificate =
            Certificate.getInstance(Base64.getDecoder().decode(caData.publicKey()));

        /**
         * Private key generation
         */
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constants.CA_ALGORITHM);
        keyGen.initialize(Constants.CA_KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();

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
        expiryTime.add(Calendar.YEAR, createRequest.validity());

        /**
         * Set it all up
         */

        X500Principal subject = new X500Principal("CN=" + createRequest.commonName());
        X500Principal issuer = new X500Principal(caCertificate.getSubject().getEncoded());

        X509v3CertificateBuilder certificateBuilder =
            new JcaX509v3CertificateBuilder(issuer, new BigInteger(Long.toString(timeNow)),
                startDate, expiryTime.getTime(), subject, keyPair.getPublic());
        if (createRequest.certificateType().equals(CertificateType.CLIENT)) {
          certificateBuilder.addExtension(Extension.extendedKeyUsage, true,
              new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_clientAuth})
                  .getEncoded());
        } else if (createRequest.certificateType().equals(CertificateType.SERVER)) {
          certificateBuilder.addExtension(Extension.extendedKeyUsage, true,
              new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth})
                  .getEncoded());
        }
        ContentSigner contentSigner = new JcaContentSignerBuilder(Constants.CA_SIGNING_ALGORITHM)
            .setProvider(bcProvider).build(keyPair.getPrivate());

        /**
         * Show time baby!
         */
        Certificate resultCert = certificateBuilder.build(contentSigner).toASN1Structure();

        String pemCertificate =
            new StringBuilder().append("-----BEGIN RSA PRIVATE KEY-----\n")
                .append(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
                    .replaceAll(".{80}", "$0\n"))
                .append("\n-----END RSA PRIVATE KEY-----\n").append("-----BEGIN CERTIFICATE-----\n")
                .append(Base64.getEncoder().encodeToString(resultCert.getEncoded())
                    .replaceAll(".{80}", "$0\n"))
                .append("\n-----END CERTIFICATE-----\n").append("-----BEGIN CERTIFICATE-----\n")
                .append(Base64.getEncoder().encodeToString(caCertificate.getEncoded())
                    .replaceAll(".{80}", "$0\n"))
                .append("\n-----END CERTIFICATE-----\n").toString();
        System.out.println(pemCertificate);
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(pemCertificate);


      } catch (NoSuchAlgorithmException e) {
        LOG.error("Invalid algorithm of {} specified, cannot initialise certificate authority!",
            Constants.CA_ALGORITHM);
      } catch (InvalidKeySpecException e) {
        LOG.error("Invalid key specification error occurred");
      } catch (OperatorCreationException e) {
        LOG.error("Operator error detected, spewing out stack trace");
        e.printStackTrace();
      } catch (IOException e) {
        LOG.error("Generic IO Exception encountered");
        e.printStackTrace();
      }

      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    } else {
      LOG.error("Unable to provide CA public key when CA is not initialised");
      return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

  }

}

package io.biza.heimdall.admin.api.impl;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import io.biza.heimdall.shared.Constants;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.security.auth.x500.X500Principal;
import javax.validation.constraints.NotNull;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;
import io.biza.heimdall.admin.api.delegate.RegisterAdministrationApiDelegate;
import io.biza.heimdall.payload.enumerations.CertificateStatus;
import io.biza.heimdall.payload.enumerations.CertificateType;
import io.biza.heimdall.payload.enumerations.JWKStatus;
import io.biza.heimdall.payload.recipient.RegisterDataRecipient;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;
import io.biza.heimdall.shared.payloads.dio.DioRegisterCertificate;
import io.biza.heimdall.shared.payloads.dio.DioRegisterJWK;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import io.biza.heimdall.shared.payloads.requests.dio.RequestJwkCreate;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.RegisterCertificateAuthorityData;
import io.biza.heimdall.shared.persistence.model.RegisterJWKData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterCertificateAuthorityRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class RegisterAdministrationApiDelegateImpl implements RegisterAdministrationApiDelegate {

  @Autowired
  RegisterJWKRepository jwkRepository;

  @Autowired
  RegisterCertificateAuthorityRepository caRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioRegisterJWK> createJwk(RequestJwkCreate jwkRequest)
      throws JoseException {
    UUID keyIdentifier = UUID.randomUUID();
    RsaJsonWebKey webKey = RsaJwkGenerator.generateJwk(2048);
    webKey.setKeyId(keyIdentifier.toString());
    RegisterJWKData registerJwkData = jwkRepository.save(RegisterJWKData.builder().id(keyIdentifier)
        .jwk(webKey.toJson(OutputControlLevel.INCLUDE_PRIVATE)).status(JWKStatus.ACTIVE).build());
    return ResponseEntity.ok(mapper.map(registerJwkData, DioRegisterJWK.class));
  }

  @Override
  public ResponseEntity<String> signCertificate(RequestCACertificateSign createRequest) {
    List<RegisterCertificateAuthorityData> caData =
        caRepository.findByStatusIn(List.of(CertificateStatus.ACTIVE));

    /**
     * Bouncycastle Registration
     */
    Provider bcProvider = new BouncyCastleProvider();
    Security.addProvider(bcProvider);

    if (caData != null && caData.size() > 0) {

      PKCS8EncodedKeySpec privateKeySpec =
          new PKCS8EncodedKeySpec(Base64.getDecoder().decode(caData.get(0).privateKey()));
      try {
        PrivateKey caPrivateKey =
            KeyFactory.getInstance(Constants.CA_ALGORITHM).generatePrivate(privateKeySpec);

        Certificate caCertificate =
            Certificate.getInstance(Base64.getDecoder().decode(caData.get(0).publicKey()));

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
        if(createRequest.certificateType().equals(CertificateType.CLIENT)) {
          certificateBuilder.addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(new KeyPurposeId[]{ KeyPurposeId.id_kp_clientAuth }).getEncoded());
        } else if(createRequest.certificateType().equals(CertificateType.SERVER)) {
          certificateBuilder.addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(new KeyPurposeId[]{ KeyPurposeId.id_kp_serverAuth }).getEncoded());
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

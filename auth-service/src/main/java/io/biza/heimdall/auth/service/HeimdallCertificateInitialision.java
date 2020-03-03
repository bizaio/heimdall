package io.biza.heimdall.auth.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springdoc.core.SpringDocWebMvcConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.biza.babelfish.cdr.enumerations.register.CertificateStatus;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityTLSData;
import io.biza.heimdall.shared.persistence.repository.RegisterAuthorityTLSRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeimdallCertificateInitialision implements InitializingBean {

  @Autowired
  RegisterAuthorityTLSRepository caRepository;
  
  @Value("${server.ssl.key-store-password}")
  public String KEYSTORE_PASSWORD;

  @Value("${server.ssl.key-store}")
  public String KEYSTORE_PATH;
  
  @Value("${heimdall.hostname}")
  public String HOSTNAME;
  
  @Override
  public void afterPropertiesSet() throws Exception {

    // By the time this runner happens the loader should have initialised
    if (new File(KEYSTORE_PATH).exists()) {
      LOG.info("Keystore {} appears to already exist, moving on", KEYSTORE_PATH);
    }

    LOG.info("Initialising the certificate for the service with hostname of {}", HOSTNAME);

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    char[] pwdArray = KEYSTORE_PASSWORD.toCharArray();
    ks.load(null, pwdArray);

    RegisterAuthorityTLSData caData =
        caRepository.findFirstByStatusIn(List.of(CertificateStatus.ACTIVE));

    /**
     * Bouncycastle Registration
     */
    Provider bcProvider = new BouncyCastleProvider();
    Security.addProvider(bcProvider);

    PKCS8EncodedKeySpec privateKeySpec =
        new PKCS8EncodedKeySpec(Base64.getDecoder().decode(caData.privateKey()));
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
    expiryTime.add(Calendar.YEAR, 1);

    /**
     * Set it all up
     */

    X500Principal subject = new X500Principal("CN=" + HOSTNAME);
    X500Principal issuer = new X500Principal(caCertificate.getSubject().getEncoded());

    X509v3CertificateBuilder certificateBuilder =
        new JcaX509v3CertificateBuilder(issuer, new BigInteger(Long.toString(timeNow)), startDate,
            expiryTime.getTime(), subject, keyPair.getPublic());
    certificateBuilder.addExtension(Extension.extendedKeyUsage, true,
        new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth}).getEncoded());
    ContentSigner contentSigner = new JcaContentSignerBuilder(Constants.CA_SIGNING_ALGORITHM)
        .setProvider(bcProvider).build(keyPair.getPrivate());

    /**
     * Show time baby!
     */
    Certificate resultCert = certificateBuilder.build(contentSigner).toASN1Structure();

    /**
     * So we have the CA and Service Certificates
     */
    X509Certificate[] certificateChain = new X509Certificate[2];
    certificateChain[0] = (X509Certificate) CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(resultCert.getEncoded()));
    certificateChain[1] = (X509Certificate) CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(caCertificate.getEncoded()));

    /**
     * Save this keystore
     */
    FileOutputStream fos = new FileOutputStream(KEYSTORE_PATH);
    ks.setKeyEntry("heimdall-service", keyPair.getPrivate(), KEYSTORE_PASSWORD.toCharArray(),
        certificateChain);
    ks.store(fos, pwdArray);
    fos.close();

    LOG.info("Certificate has been initialised in the keystore with CN of {}", HOSTNAME);

    return;

  }
}

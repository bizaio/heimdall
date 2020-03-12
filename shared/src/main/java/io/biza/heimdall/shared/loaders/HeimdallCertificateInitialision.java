package io.biza.heimdall.shared.loaders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Autowired
  SecurityCredentialSetup credentialsSetup;

  @Value("${server.ssl.key-store-password}")
  public String KEYSTORE_PASSWORD;

  @Value("${server.ssl.key-store}")
  public String KEYSTORE_PATH;

  @Value("${heimdall.ssl-initialiser-hostname:localhost}")
  public String HOSTNAME;
  
  @Value("${server.ssl.key-alias:heimdall-service}")
  public String keystoreServiceKey;
  
  @Value("${heimdall.ca-keystore-id:heimdall-ca}")
  public String keystoreCaServiceKey;

  @Override
  public void afterPropertiesSet() throws Exception {

    // Forcibly initialise credentials
    credentialsSetup.initialiseSecurityCredentials();

    // By the time this runner happens the loader should have initialised
    if (new File(KEYSTORE_PATH).exists()) {
      LOG.info("Keystore {} appears to already exist, moving on", KEYSTORE_PATH);
      FileInputStream fos = new FileInputStream(KEYSTORE_PATH);
      KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      keystore.load(fos, KEYSTORE_PASSWORD.toCharArray());
      Key key = keystore.getKey(keystoreServiceKey, KEYSTORE_PASSWORD.toCharArray());
      if (key instanceof PrivateKey) {
        // Get certificate of public key
        java.security.cert.Certificate cert = keystore.getCertificate(keystoreServiceKey);
        // Get public key
        PublicKey publicKey = cert.getPublicKey();

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        LOG.info(
            "Server certificate details are set to:\n\n-----BEGIN CERTIFICATE-----\n{}\n-----END CERTIFICATE-----\n\n",
            publicKeyString.replaceAll(".{80}(?=.)", "$0\n"));

      }
      fos.close();
    }

    LOG.info("Initialising the certificate for the service with hostname of {}", HOSTNAME);

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    char[] pwdArray = KEYSTORE_PASSWORD.toCharArray();
    ks.load(null, pwdArray);

    RegisterAuthorityTLSData caData =
        caRepository.findFirstByStatusIn(List.of(CertificateStatus.ACTIVE));

    if (caData == null) {
      LOG.error("CA Data is not initialised, cannot proceed!");
      return;
    }

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
    ContentSigner caSigner = new JcaContentSignerBuilder(Constants.CA_SIGNING_ALGORITHM)
        .setProvider(bcProvider).build(caPrivateKey);

    /**
     * Show time baby!
     */
    Certificate resultCert = certificateBuilder.build(caSigner).toASN1Structure();

    /**
     * So we have the CA and Service Certificates
     */
    X509Certificate[] certificateChain = new X509Certificate[2];
    certificateChain[0] = (X509Certificate) CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(resultCert.getEncoded()));
    certificateChain[1] = (X509Certificate) CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(caCertificate.getEncoded()));
    
    /**
     * Load the CA on it's own so we can validate our own certificates
     */
    X509Certificate caCertificateStore = (X509Certificate) CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(caCertificate.getEncoded()));
    
    /**
     * Save this keystore
     */
    FileOutputStream fos = new FileOutputStream(KEYSTORE_PATH);
    ks.setKeyEntry(keystoreServiceKey, keyPair.getPrivate(), KEYSTORE_PASSWORD.toCharArray(),
        certificateChain);
    ks.setCertificateEntry(keystoreCaServiceKey, caCertificateStore);
    ks.store(fos, pwdArray);
    fos.close();

    LOG.info("Certificate has been initialised in the keystore with CN of {}", HOSTNAME);

    return;

  }
}

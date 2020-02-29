package io.biza.heimdall.shared.loaders;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.biza.heimdall.payload.enumerations.CertificateStatus;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.persistence.model.RegisterCertificateAuthorityData;
import io.biza.heimdall.shared.persistence.repository.RegisterCertificateAuthorityRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(10)
public class HeimdallCertificateInitialision implements ApplicationRunner {

  @Autowired
  RegisterCertificateAuthorityRepository caRepository;

  public static void main(String[] args) {
    SpringApplication.run(HeimdallCertificateInitialision.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    
    if(!args.getOptionNames().contains("initcert")) {
      LOG.info("Certificate Initialisation is not requested: {}", args.getOptionNames());
      return;
    }
    
    List<String> hostname = args.getOptionValues("initcert");
    
    LOG.warn("Initialising the certificate for the service with hostname of {}", hostname);
    
    // By the time this runner happens the loader should have initialised
    if (new File(Constants.LOCAL_KEYSTORE_PATH).exists()) {
      LOG.error("Keystore appears to already exist, aborting");
      return;
    }
    

    if (hostname.size() != 1) {
      LOG.error("One and Only one hostname should be supplied");
      System.exit(0);
    }

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    char[] pwdArray = Constants.LOCAL_KEYSTORE_PASSWORD.toCharArray();
    ks.load(null, pwdArray);

    List<RegisterCertificateAuthorityData> caData =
        caRepository.findByStatusIn(List.of(CertificateStatus.ACTIVE));

    /**
     * Bouncycastle Registration
     */
    Provider bcProvider = new BouncyCastleProvider();
    Security.addProvider(bcProvider);

    PKCS8EncodedKeySpec privateKeySpec =
        new PKCS8EncodedKeySpec(Base64.getDecoder().decode(caData.get(0).privateKey()));
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
    expiryTime.add(Calendar.YEAR, 1);

    /**
     * Set it all up
     */

    X500Principal subject = new X500Principal("CN=" + hostname.get(0));
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
    FileOutputStream fos = new FileOutputStream(Constants.LOCAL_KEYSTORE_PATH);
    LOG.info("Writing the following certificate chain: {}", certificateChain.toString());
    ks.setKeyEntry("heimdall-service", keyPair.getPrivate(),
        Constants.LOCAL_KEYSTORE_PASSWORD.toCharArray(), certificateChain);
    ks.store(fos, pwdArray);
    fos.close();

    LOG.info("Certificate has been initialised in the keystore with CN of {}", hostname.get(0));
    
    System.exit(0);

  }

}

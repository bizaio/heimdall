package io.biza.heimdall.auth.test;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import io.biza.heimdall.auth.test.token.ClientCredentialsTokenIT;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SpringTestEnvironment {
  
  public URI getIssuerUri() {
    return URI.create("https://localhost:" + System.getProperty("local.server.port") + "/dio-auth");
  }

  public SSLContext trustAllCerts() {
    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }
      public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
      public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
    }};


    try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, trustAllCerts, new SecureRandom());
      return sslContext;
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      LOG.error("Setup of custom SSL Context failed, results may vary from here");
      return null;
    }

  }
}

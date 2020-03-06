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
}

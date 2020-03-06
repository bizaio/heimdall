package io.biza.heimdall.register.test;

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
import io.biza.heimdall.register.test.holder.EndpointsWithCredentialsIT;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.thumb.client.Thumb;
import io.biza.thumb.client.ThumbConfig;
import io.biza.thumb.client.ThumbRegisterConfig;
import io.biza.thumb.client.enumerations.ThumbRegisterMode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringTestEnvironment {
  
  public Thumb getHolderThumb() {
    LOG.info("Auth URI: {}", System.getProperty("register-auth-uri"));
    LOG.info("Resource URI: {}", System.getProperty("register-resource-uri"));
    
    URI authUri = URI.create(System.getProperty("register-auth-uri"));
    URI resourceUri = URI.create(System.getProperty("register-resource-uri"));
    
    return Thumb.builder()
        .config(ThumbConfig.builder().sslVerify(false)
            .register(ThumbRegisterConfig.builder().authUri(authUri)
                .resourceUri(resourceUri)
                .clientId(TestDataConstants.HOLDER_CLIENT_ID)
                .clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET).mode(ThumbRegisterMode.HOLDER)
                .build())
            .build())
        .build();

  }
}

package io.biza.heimdall.register.api.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ClientTrustStoreConfiguration implements InitializingBean {
    @Autowired
    Environment env;    

    @Override
    public void afterPropertiesSet() throws Exception {
      LOG.info("Wiring client trust store to use server trust store {}", env.getProperty("server.ssl.key-store"));
      System.setProperty("https.protocols", "TLSv1.2");
      System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.key-store")); 
      System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.key-store-password"));      
    }
}
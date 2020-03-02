package io.biza.heimdall.shared;

import org.jose4j.jws.AlgorithmIdentifiers;

public class Constants {
  public static final String DEFAULT_LOCALE = "AU";
  public static final String DEFAULT_LANGUAGE = "en";
  
  public static final String CA_ALGORITHM = "RSA";
  public static final int CA_KEY_SIZE = 2048;
  public static final int CA_VALIDITY_YEARS = 10;
  public static final String CA_DN = "CN=Heimdall Development Register CA";
  public static final String CA_SIGNING_ALGORITHM = "SHA256WithRSA";
  
  public static final String JAVA_ALGORITHM = "RSASSA-PSS";
  public static final String JOSE4J_ALGORITHM = AlgorithmIdentifiers.RSA_PSS_USING_SHA256;
  
  public static final String LOCAL_KEYSTORE_PASSWORD = "solongandthanksforallthefish";
  public static final String LOCAL_KEYSTORE_PATH = "heimdall.jks";
  
}

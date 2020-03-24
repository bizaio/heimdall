package io.biza.heimdall.shared;

import java.util.List;

public class Constants {
  public static final String DEFAULT_LOCALE = "AU";
  public static final String DEFAULT_LANGUAGE = "en";

  public static final String CA_ALGORITHM = "RSA";
  public static final int CA_KEY_SIZE = 2048;
  public static final int CA_VALIDITY_YEARS = 10;
  public static final String CA_DN = "CN=Heimdall Development Register CA";
  public static final String CA_SIGNING_ALGORITHM = "SHA256WithRSA";

  public static final String JAVA_ALGORITHM = "RSASSA-PSS";
  public static final String LOCAL_KEYSTORE_PASSWORD = "solongandthanksforallthefish";
  public static final String LOCAL_KEYSTORE_PATH = "heimdall.jks";

  public final static String SECURITY_SCOPE_REGISTER_BANK_READ = "cdr-register:bank:read";
  public final static String OAUTH2_SCOPE_REGISTER_BANK_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_REGISTER_BANK_READ + "')";

  public final static String REGISTER_ISSUER = "dio-register";
  public final static long REGISTER_SSA_LENGTH_HOURS = 1;
  public final static List<String> REGISTER_SCOPE_LIST = List.of(SECURITY_SCOPE_REGISTER_BANK_READ);



}

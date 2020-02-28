package io.biza.heimdall.register;

public class Constants {
  
  public static final String OPENID_CONNECT_URL =
      "http://localhost:9090/auth/realms/master/.well-known/openid-configuration";
  
  /**
   * Tag definitions
   */
  public final static String TAG_BANKING_DATA_HOLDER_NAME = "banking-data-holder";
  public final static String TAG_BANKING_DATA_HOLDER_DESCRIPTION = "Banking Data Holder API";
  public final static String TAG_BANKING_DATA_RECIPIENT_NAME = "banking-data-recipient";
  public final static String TAG_BANKING_DATA_RECIPIENT_DESCRIPTION = "Banking Data Recipient API";
  public final static String TAG_BANKING_REGISTER_NAME = "register";
  public final static String TAG_BANKING_REGISTER_DESCRIPTION = "Register Functions";


  /**
   * Security labels
   */
  public final static String SECURITY_SCHEME_NAME = "heimdall_auth";
  public final static String SECURITY_SCOPE_HOLDER_READ = "HEIMDALL:ADMIN:HOLDER:READ";
  public final static String OAUTH2_SCOPE_HOLDER_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_HOLDER_READ + "')";
  public final static String SECURITY_SCOPE_HOLDER_WRITE = "HEIMDALL:ADMIN:HOLDER:WRITE";
  public final static String OAUTH2_SCOPE_HOLDER_WRITE =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_HOLDER_WRITE + "')";
  public final static String SECURITY_SCOPE_RECIPIENT_READ = "HEIMDALL:ADMIN:RECIPIENT:READ";
  public final static String OAUTH2_SCOPE_RECIPIENT_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_RECIPIENT_READ + "')";
  public final static String SECURITY_SCOPE_RECIPIENT_WRITE = "HEIMDALL:ADMIN:RECIPIENT:WRITE";
  public final static String OAUTH2_SCOPE_RECIPIENT_WRITE =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_RECIPIENT_WRITE + "')";

  /**
   * Response codes as strings
   */
  public final static String RESPONSE_CODE_CREATED = "201";
  public final static String RESPONSE_CODE_OK = "200";
  public final static String RESPONSE_CODE_NOT_FOUND = "404";
  public final static String RESPONSE_CODE_NO_CONTENT = "204";
  public final static String RESPONSE_CODE_UNPROCESSABLE_ENTITY = "422";

  /**
   * Register Constants
   */
  public final static String REGISTER_ISSUER = "dio-register";
  public final static long REGISTER_SSA_LENGTH_HOURS = 1;
  

}

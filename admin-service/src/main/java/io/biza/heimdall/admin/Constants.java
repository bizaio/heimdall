package io.biza.heimdall.admin;

public class Constants {
	
  public static final String VERSION = "1.1.0-SNAPSHOT";	

  public static final String OPENID_CONNECT_URL =
      "http://localhost:9090/auth/realms/master/.well-known/openid-configuration";

  /**
   * Tag definitions
   */
  public final static String TAG_DATA_HOLDER_NAME = "data-holder";
  public final static String TAG_DATA_HOLDER_DESCRIPTION = "Data Holder API";
  public final static String TAG_DATA_RECIPIENT_NAME = "data-recipient";
  public final static String TAG_DATA_RECIPIENT_DESCRIPTION = "Data Recipient API";
  public final static String TAG_DATA_REGISTER_ADMIN_NAME = "register-admin";
  public final static String TAG_DATA_REGISTER_ADMIN_DESCRIPTION = "Register Administration API";

  /**
   * Error Descriptions
   */
  public final static String ERROR_INVALID_HOLDER = "Invalid Holder Identifier Specified";
  public final static String ERROR_INVALID_RECIPIENT = "Invalid Recipient Identifier Specified";
  public final static String ERROR_INVALID_BRAND =
      "Invalid Recipient and Brand identifier combinations specified";


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
  public final static String SECURITY_SCOPE_KEY_ADMIN = "HEIMDALL:ADMIN:KEY:ADMIN";
  public final static String OAUTH2_SCOPE_KEY_ADMIN =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_KEY_ADMIN + "')";
}

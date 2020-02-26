package io.biza.heimdall.admin;

public class Constants {
  
  public static final String OPENID_CONNECT_URL =
      "http://localhost:9090/auth/realms/master/.well-known/openid-configuration";
  
  /**
   * Tag definitions
   */
  public final static String TAG_DATA_HOLDER_NAME = "data-holder";
  public final static String TAG_DATA_HOLDER_DESCRIPTION = "Data Holder API";
  public final static String TAG_DATA_RECIPIENT_NAME = "data-recipient";
  public final static String TAG_DATA_RECIPIENT_DESCRIPTION = "Data Recipient API";


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
   * Response descriptions
   */
  public final static String RESPONSE_SUCCESSFUL_LIST =
      "Successful Response containing list of requested objects";
  public final static String RESPONSE_SUCCESSFUL_READ =
      "Success";
  public final static String RESPONSE_SUCCESSFUL_CREATE =
      "Successfully created new object with content returned";
  public final static String RESPONSE_SUCCESSFUL_DELETE =
      "Successfully deleted object specified in request with no content returned";
  public final static String RESPONSE_SUCCESSFUL_UPDATE =
      "Successfully updated object specified with updated object returned";
  public final static String RESPONSE_OBJECT_NOT_FOUND = "Requested Object could not be found";
  public final static String RESPONSE_INPUT_VALIDATION_ERROR =
      "Provided request details contains validation errors, validation errors are included in the response";
}

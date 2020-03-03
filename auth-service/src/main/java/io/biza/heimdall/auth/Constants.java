package io.biza.heimdall.auth;

import java.net.URI;
import java.util.List;

public class Constants {

  public static final String OPENID_CONNECT_URL =
      "https://localhost:9103/.well-known/openid-configuration";

  /**
   * Tag definitions
   */
  public final static String TAG_DISCOVERY_NAME = "discovery";
  public final static String TAG_DISCOVERY_DESCRIPTION = "Discovery Endpoints";
  public final static String TAG_CA_NAME = "certificate-authority";
  public final static String TAG_CA_DESCRIPTION = "CA Endpoints";
  public final static String TAG_TOKEN_NAME = "token";
  public final static String TAG_TOKEN_DESCRIPTION = "Token Authorisation Endpoints";
  
  /**
   * Response codes as strings
   */
  public final static String RESPONSE_CODE_CREATED = "201";
  public final static String RESPONSE_CODE_OK = "200";
  public final static String RESPONSE_CODE_NOT_FOUND = "404";
  public final static String RESPONSE_CODE_NO_CONTENT = "204";
  public final static String RESPONSE_CODE_UNPROCESSABLE_ENTITY = "422";

  /**
   * Security Scopes
   */
  public final static String SECURITY_SCHEME_NAME = "heimdall_auth";
  public final static String SECURITY_SCOPE_REGISTER_BANK_READ = "cdr-register:bank:read";
  
  /**
   * Register Constants
   */
  public final static List<String> REGISTER_SCOPE_LIST = List.of(SECURITY_SCOPE_REGISTER_BANK_READ);

  /**
   * OAuth2 Errors
   */
  public final static URI OAUTH2_ERROR_RESPONSE_URI =
      URI.create("https://tools.ietf.org/html/rfc6749#section-5.2");
  public final static String OAUTH2_INVALID_REQUEST_MESSAGE =
      "The request is missing a required parameter, includes an unsupported parameter value (other than grant type), repeats a parameter, includes multiple credentials, utilizes more than one mechanism for authenticating the client, or is otherwise malformed.";
  public final static String OAUTH2_INVALID_CLIENT_MESSAGE =
      "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";
  public final static String OAUTH2_INVALID_GRANT_MESSAGE =
      "The provided authorization grant (e.g., authorization code, resource owner credentials) or refresh token is invalid, expired, revoked, does not match the redirection URI used in the authorization request, or was issued to another client.";
  public final static String OAUTH2_UNAUTHORIZED_CLIENT_MESSAGE =
      "The authenticated client is not authorized to use this authorization grant type.";
  public final static String OAUTH2_UNSUPPORTED_GRANT_TYPE_MESSAGE =
      "The authorization grant type is not supported by the authorization server.";
  public final static String OAUTH2_INVALID_SCOPE_MESSAGE =
      "The requested scope is invalid, unknown, malformed, or exceeds the scope granted by the resource owner.";

}

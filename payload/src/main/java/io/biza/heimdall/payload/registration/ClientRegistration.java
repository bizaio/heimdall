    package io.biza.heimdall.payload.registration;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.EpochToOffsetDateTimeConverter;
import io.biza.babelfish.cdr.converters.OffsetDateTimeToEpochConverter;
import io.biza.babelfish.cdr.converters.UriStringToUriConverter;
import io.biza.babelfish.cdr.converters.UriToUriStringConverter;
import io.biza.heimdall.payload.enumerations.ClientAuthenticationMethodType;
import io.biza.heimdall.payload.enumerations.JWEEncryptionAlgorithmType;
import io.biza.heimdall.payload.enumerations.JWEEncryptionMethodType;
import io.biza.heimdall.payload.enumerations.JWSSigningAlgorithmType;
import io.biza.heimdall.payload.enumerations.OAuth2ResponseType;
import io.biza.heimdall.payload.enumerations.OIDCApplicationType;
import io.biza.heimdall.payload.enumerations.OIDCGrantType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "An individual software product with status")
public class ClientRegistration {

  @JsonProperty("iss")
  @NotEmpty
  @Schema(description = "Unique identifier for the Data Holder issued by the CDR Register")
  String iss;

  @JsonProperty("iat")
  @JsonSerialize(converter = OffsetDateTimeToEpochConverter.class)
  @JsonDeserialize(converter = EpochToOffsetDateTimeConverter.class)
  @Schema(
      description = "The time at which the request was issued by the TPP expressed as seconds since 1970-01-01T00:00:00Z as measured in UTC")
  OffsetDateTime iat;

  @JsonProperty("exp")
  @JsonSerialize(converter = OffsetDateTimeToEpochConverter.class)
  @JsonDeserialize(converter = EpochToOffsetDateTimeConverter.class)
  @Schema(
      description = "The time at which the request expires expressed as seconds since 1970-01-01T00:00:00Z as measured in UTC")

  OffsetDateTime exp;

  @JsonProperty("jti")
  @NotEmpty
  @Schema(description = "Unique identifier for the JWT, used to prevent replay of the token")
  String jti;

  @JsonProperty("aud")
  @NotEmpty
  @Schema(
      description = "The audience for the request. This should be the Data Holder authorisation server URI")
  String aud;

  @JsonProperty("client_id")
  @NotEmpty
  @Schema(description = "Data Holder issued client identifier string")
  String clientId;

  @JsonProperty("client_id_issued_at")
  @JsonSerialize(converter = OffsetDateTimeToEpochConverter.class)
  @JsonDeserialize(converter = EpochToOffsetDateTimeConverter.class)
  @Schema(
      description = "Time at which the client identifier was issued expressed as seconds since 1970-01-01T00:00:00Z as measured in UTC")
  @Builder.Default
  OffsetDateTime issuedAt = OffsetDateTime.now();

  @JsonProperty("client_name")
  @NotEmpty
  @Schema(
      description = "Human-readable string name of the software product to be presented to the end-user during authorization")
  String clientName;

  @JsonProperty("client_description")
  @NotEmpty
  @Schema(
      description = "Human-readable string name of the software product description to be presented to the end user during authorization")
  String clientDescription;

  @JsonProperty("client_uri")
  @NotEmpty
  @Schema(description = "URL string of a web page providing information about the client")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI clientUri;

  @JsonProperty("org_id")
  @NotEmpty
  @Schema(
      description = "A unique identifier string assigned by the CDR Register that identifies the Accredited Data Recipient Brand")
  String organisationId;

  @JsonProperty("org_name")
  @NotEmpty
  @Schema(
      description = "Human-readable string name of the Accredited Data Recipient to be presented to the end user during authorization")
  String organisationName;

  @JsonProperty("redirect_uris")
  @NotEmpty
  @Schema(description = "Array of redirection URI strings for use in redirect-based flows")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  List<URI> redirectUris;

  @JsonProperty("logo_uri")
  @NotEmpty
  @Schema(
      description = "URL string that references a logo for the client. If present, the server SHOULD display this image to the end-user during approval")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI logoUri;

  @JsonProperty("tos_uri")
  @NotEmpty
  @Schema(
      description = "URL string that points to a human-readable terms of service document for the Software Product")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI tosUri;

  @JsonProperty("policy_uri")
  @NotEmpty
  @Schema(
      description = "URL string that points to a human-readable policy document for the Software Product")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI policyUri;

  @JsonProperty("jwks_uri")
  @NotEmpty
  @Schema(
      description = "URL string referencing the client JSON Web Key (JWK) Set [RFC7517] document, which contains the client public keys")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI jwksUri;

  @JsonProperty("revocation_uri")
  @NotEmpty
  @Schema(
      description = "URI string that references the location of the Software Product consent revocation endpoint")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI revocationUri;

  @JsonProperty("token_endpoint_auth_method")
  @NotEmpty
  @Schema(description = "The requested authentication method for the token endpoint")
  ClientAuthenticationMethodType tokenEndpointAuthMethod;

  @JsonProperty("token_endpoint_auth_signing_alg")
  @NotEmpty
  @Schema(description = "The algorithm used for signing the JWT")
  JWSSigningAlgorithmType tokenEndpointAuthSigningAlgorithm;

  @JsonProperty("grant_types")
  @NotEmpty
  @Schema(
      description = "Array of OAuth 2.0 grant type strings that the client can use at the token endpoint")
  List<List<OIDCGrantType>> grantTypes;

  @JsonProperty("response_types")
  @NotEmpty
  @Schema(
      description = "Array of the OAuth 2.0 response type strings that the client can use at the authorization endpoint.")
  List<OAuth2ResponseType> responseTypes;

  @JsonProperty("application_type")
  @NotNull
  @Schema(description = "Kind of the application. The only supported application type will be web")
  OIDCApplicationType applicationType;

  @JsonProperty("id_token_signed_response_alg")
  @Schema(description = "ID Token JWS Signing Algorithms Supported")
  @Builder.Default
  @NotEmpty
  List<JWSSigningAlgorithmType> idTokenSigningAlgorithms = List.of(JWSSigningAlgorithmType.PS256);

  @JsonProperty("id_token_encrypted_response_alg")
  @Schema(description = "ID Token JWE Encryption Algorithms Supported")
  List<JWEEncryptionAlgorithmType> idTokenEncryptionAlgorithms;

  @JsonProperty("id_token_encrypted_response_enc")
  @Schema(description = "ID Token JWE Encryption Algorithms Supported")
  List<JWEEncryptionMethodType> idTokenEncryptionMethods;

  @JsonProperty("request_object_signing_alg")
  @Schema(description = "Request Object Signing Algorithms")
  List<JWSSigningAlgorithmType> requestObjectSigningAlgorithms;

  @JsonProperty("software_statement")
  @Schema(description = "The Software Statement Assertion")
  SoftwareStatementAssertion ssa;

  @JsonProperty("software_id")
  @Schema(
      description = "String representing a unique identifier assigned by the ACCC Register and used by registration endpoints to identify the software product to be dynamically registered.")
  String softwareId;

  @JsonProperty("scope")
  @NotNull
  @Schema(
      description = "String containing a space-separated list of scope values that the client can use when requesting access tokens.")
  List<String> scope;

}

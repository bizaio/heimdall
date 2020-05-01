package io.biza.heimdall.shared.payloads.dio;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;
import io.biza.babelfish.cdr.enumerations.register.RegisterSoftwareRole;
import io.biza.babelfish.cdr.enumerations.register.SoftwareProductStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
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
@Schema(description = "Data Recipient Brand Software Products")
public class DioSoftwareProduct {

  @JsonProperty("id")
  @Schema(description = "Data Recipient Software Product Identifier", accessMode = AccessMode.READ_ONLY)
  UUID id;

  @JsonProperty("status")
  @NotNull
  SoftwareProductStatusType status;

  @JsonProperty("name")
  @NotEmpty
  @Schema(description = "Software Product Name")
  String name;

  @JsonProperty("description")
  @NotEmpty
  @Schema(description = "Software Product Description")
  String description;

  @JsonProperty("uri")
  @Schema(description = "Software Product URI")
  @NotNull
  URI uri;

  @JsonProperty("redirectUris")
  @Schema(description = "Software Product Redirect URI List")
  @NotNull
  List<URI> redirectUris;

  @JsonProperty("logoUri")
  @Schema(description = "Software Product Logo URI")
  @NotNull
  URI logoUri;

  @JsonProperty("jwksUri")
  @Schema(description = "JWKS URI")
  @NotNull
  URI jwksUri;


  @JsonProperty("tosUri")
  @Schema(description = "Software Product TOS URI")
  @NotNull
  URI tosUri;

  @JsonProperty("policyUri")
  @Schema(description = "Software Product Policy URI")
  @NotNull
  URI policyUri;

  @JsonProperty("revocationUri")
  @Schema(description = "Software Product Revocation URI")
  @NotNull
  URI revocationUri;

  @JsonProperty("softwareRole")
  @Schema(description = "Software Product TOS URI")
  @NotNull
  @Builder.Default
  RegisterSoftwareRole softwareRole = RegisterSoftwareRole.DATA_RECIPIENT_SOFTWARE_PRODUCT;

  @JsonProperty("scopes")
  @Schema(description = "Scopes this Software Product has access to")
  List<CDRScope> scopes;

}

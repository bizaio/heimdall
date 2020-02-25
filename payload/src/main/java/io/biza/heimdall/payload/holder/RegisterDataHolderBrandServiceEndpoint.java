package io.biza.heimdall.payload.holder;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.UriStringToUriConverter;
import io.biza.babelfish.cdr.converters.UriToUriStringConverter;
import io.biza.heimdall.payload.common.LegalEntityDetail;
import io.biza.heimdall.payload.enumerations.DataHolderStatusType;
import io.biza.heimdall.payload.enumerations.IndustryType;
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
@Schema(description = "Endpoints related to Data Holder Brand services")
public class RegisterDataHolderBrandServiceEndpoint {
  
  @JsonProperty("version")
  @NotEmpty
  @Schema(
      description = "The major version of the high level standards. This is not the version of the endpoint or the payload being requested but the version of the overall standards being applied. This version number will be \"v\" followed by the major version of the standards as a positive integer (e.g. v1, v12 or v76)")
  String version;
  
  @Schema(
      description = "Base URI for the Data Holder's Consumer Data Standard public endpoints",
      type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  @JsonProperty("publicBaseUri")
  @Valid
  @NotNull
  URI publicBaseUri;
  
  @Schema(
      description = "Base URI for the Data Holder's Consumer Data Standard resource endpoints",
      type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  @JsonProperty("resourceBaseUri")
  @Valid
  @NotNull
  URI resourceBaseUri;
  
  
  @Schema(
      description = "Base URI for the Data Holder's Consumer Data Standard information security endpoints",
      type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  @JsonProperty("infosecBaseUri")
  @Valid
  @NotNull
  URI infosecBaseUri;
  
  @Schema(
      description = "Base URI for the Data Holder extension endpoints to the Consumer Data Standard (optional)",
      type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  @JsonProperty("extensionBaseUri")
  @Valid
  URI extensionBaseUri;
  
  @Schema(
      description = "Publicly available website or web resource URI",
      type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  @JsonProperty("websiteUri")
  @Valid
  @NotNull
  URI websiteUri;

}

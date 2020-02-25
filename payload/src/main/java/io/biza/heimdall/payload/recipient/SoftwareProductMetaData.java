package io.biza.heimdall.payload.recipient;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.UriStringToUriConverter;
import io.biza.babelfish.cdr.converters.UriToUriStringConverter;
import io.biza.heimdall.payload.enumerations.SoftwareProductStatusType;
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
public class SoftwareProductMetaData {
  
  @JsonProperty("softwareProductId")
  @NotEmpty
  @Schema(
      description = "Unique Software Product Identifier")
  String softwareProductId;
  
  @JsonProperty("softwareProductName")
  @NotEmpty
  @Schema(
      description = "Name of the software product")
  String softwareProductName;
  
  @JsonProperty("softwareProductDescription")
  @NotEmpty
  @Schema(
      description = "Description of the software product")
  String softwareProductDescription;
  
  @JsonProperty("logoUri")
  @Schema(description = "Data Recipient Brand logo URI", type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI logoUri;
  
  @JsonProperty("softwareProductStatus")
  @NotNull
  @Schema(
      description = "Software Product Status")
  SoftwareProductStatusType status;
  
}

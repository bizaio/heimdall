package io.biza.heimdall.payload.recipient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
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
import io.biza.heimdall.payload.enumerations.DataRecipientBrandStatusType;
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
public class DataRecipientBrandMetaData {
  
  @JsonProperty("dataRecipientBrandId")
  @NotEmpty
  @Schema(
      description = "Unique id of the Data Recipient brand")
  String dataRecipientBrandId;
  
  @JsonProperty("brandName")
  @NotEmpty
  @Schema(
      description = "Data Recipient Brand name")
  String brandName;
  
  @JsonProperty("logoUri")
  @Schema(description = "Data Recipient Brand logo URI", type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI logoUri;
  
  @JsonProperty("softwareProducts")
  @Schema(description = "Data Recipient Brand Software Products")
  SoftwareProductMetaData softwareProducts;
  
  @JsonProperty("status")
  @Schema(description = "Data Recipient Brand Status")
  DataRecipientBrandStatusType status;
  
}

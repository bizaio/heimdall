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
import io.biza.heimdall.payload.enumerations.DataRecipientStatusType;
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
@Schema(description = "A Single Data Holder Brand")
public class RegisterDataRecipient {

  @JsonProperty("legalEntityId")
  @NotEmpty
  @Schema(description = "Unique id of the Data Recipient Legal Entity")
  String legalEntityId;

  @JsonProperty("legalEntityName")
  @NotEmpty
  @Schema(description = "Legal name of the Data Recipient")
  String legalEntityName;

  @JsonProperty("industry")
  @Schema(description = "Industry Type")
  @Builder.Default
  IndustryType industry = IndustryType.BANKING;

  @JsonProperty("logoUri")
  @Schema(description = "Legal Entity Logo URI", type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  URI logoUri;

  @JsonProperty("dataRecipientBrands")
  @Schema(description = "Endpoints related to Data Holder Brand services")
  List<DataRecipientBrandMetaData> dataRecipientBrands;

  @JsonProperty("status")
  @Schema(description = "Data Recipient Brand Status")
  DataRecipientStatusType status;

  @JsonProperty("lastUpdated")
  @NotNull
  @Schema(
      description = "The date/time that the Data Holder Brand data was last updated in the Register",
      type = "string", format = "date-time")
  OffsetDateTime lastUpdated;

}

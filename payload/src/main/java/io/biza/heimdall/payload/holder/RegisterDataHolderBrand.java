package io.biza.heimdall.payload.holder;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Schema(description = "A Single Data Holder Brand")
public class RegisterDataHolderBrand {
  
  @JsonProperty("dataHolderBrandId")
  @NotEmpty
  @Schema(
      description = "Unique Data Holder Brand Identifier")
  String dataHolderBrandId;
  
  @JsonProperty("brandName")
  @NotEmpty
  @Schema(
      description = "Data Holder Brand Name")
  String brandName;
  
  @JsonProperty("industry")
  @Schema(description = "Industry Type")
  IndustryType industry;
  
  @JsonProperty("brandName")
  @NotNull
  @Schema(
      description = "The data that is common to all organisations, regardless of the type (e.g. company, trust, partnership, government)")
  LegalEntityDetail legalEntity;
  
  @JsonProperty("status")
  @NotNull
  @Schema(description = "Data Holder Status")
  DataHolderStatusType status;
  
  @JsonProperty("endpointDetail")
  @NotNull
  @Schema(description = "Provides the endpoint details for the brand")
  RegisterDataHolderBrandServiceEndpoint endpointDetail;
  
  @JsonProperty("authDetails")
  @NotNull
  @Schema(description = "Provides details of authorisation endpoints for Data Holders")
  List<RegisterDataHolderAuth> authDetails;
  
  @JsonProperty("lastUpdated")
  @NotNull
  @Schema(description = "The date/time that the Data Holder Brand data was last updated in the Register", type = "string", format = "date-time")
  OffsetDateTime lastUpdated;
  
}

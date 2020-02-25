package io.biza.heimdall.payload.recipient;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.heimdall.payload.common.LegalEntityDetail;
import io.biza.heimdall.payload.enumerations.DataHolderStatusType;
import io.biza.heimdall.payload.enumerations.IndustryType;
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
@Schema(description = "An individual software product with status")
public class SoftwareProductStatus {
  
  @JsonProperty("softwareProductId")
  @NotEmpty
  @Schema(
      description = "Unique Software Product Identifier")
  String softwareProductId;
  
  @JsonProperty("softwareProductStatus")
  @NotNull
  @Schema(
      description = "Software Product Status")
  SoftwareProductStatusType softwareProductStatus;

}
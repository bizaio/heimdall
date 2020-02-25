package io.biza.heimdall.payload.recipient;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.heimdall.payload.common.LegalEntityDetail;
import io.biza.heimdall.payload.enumerations.DataHolderStatusType;
import io.biza.heimdall.payload.enumerations.DataRecipientStatusType;
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
public class DataRecipientStatus {
  
  @JsonProperty("dataRecipientId")
  @NotEmpty
  @Schema(
      description = "Unique Data Recipient Identifier")
  String dataRecipientId;
  
  @JsonProperty("dataRecipientStatus")
  @NotNull
  @Schema(
      description = "Data Recipient Status")
  DataRecipientStatusType dataRecipientStatus;

}

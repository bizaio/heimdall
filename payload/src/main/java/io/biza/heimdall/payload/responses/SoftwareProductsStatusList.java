package io.biza.heimdall.payload.responses;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.heimdall.payload.common.LegalEntityDetail;
import io.biza.heimdall.payload.enumerations.DataHolderStatusType;
import io.biza.heimdall.payload.enumerations.IndustryType;
import io.biza.heimdall.payload.recipient.SoftwareProductStatus;
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
@Schema(description = "A list of Software Product Statuses")
public class SoftwareProductsStatusList {
  
  @JsonProperty("softwareProducts")
  @NotEmpty
  @Schema(
      description = "Software Products with Statuses")
  List<SoftwareProductStatus> softwareProducts;

}

package io.biza.heimdall.shared.payloads.dio;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderAuthV1;
import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrandServiceEndpointV1;
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
@Schema(description = "The Data Holder Brand")
public class DioDataHolderBrand {

  @JsonProperty("id")
  @Schema(description = "Data Holder Brand Identifier", accessMode = AccessMode.READ_ONLY)
  UUID id;

  @JsonProperty("name")
  @NotEmpty
  @Schema(description = "Brand Name")
  String name;

  @JsonProperty("logoUri")
  @NotNull
  URI logoUri;

  @JsonProperty("status")
  @NotNull
  DataRecipientStatusType status;

  @JsonProperty("endpointDetail")
  @NotNull
  RegisterDataHolderBrandServiceEndpointV1 endpointDetail;

  @JsonProperty("lastUpdated")
  OffsetDateTime lastUpdated;
  
  @JsonProperty("authDetails")
  List<RegisterDataHolderAuthV1> authDetails;
  

}

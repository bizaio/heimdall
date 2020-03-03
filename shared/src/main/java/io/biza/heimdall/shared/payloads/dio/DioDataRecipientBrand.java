package io.biza.heimdall.shared.payloads.dio;

import java.net.URI;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
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
@Schema(description = "The Data Recipient Brand")
public class DioDataRecipientBrand {
  
  @JsonProperty("id")
  @NotNull
  @Schema(description = "Data Recipient Brand Identifier")
  UUID id;
  
  @JsonProperty("name")
  @NotEmpty
  @Schema(
      description = "Brand Name")
  String name;
  
  @JsonProperty("logoUri")
  @NotNull
  URI logoUri;
  
  @JsonProperty("status")
  @NotNull
  DataRecipientStatusType status;
  
}

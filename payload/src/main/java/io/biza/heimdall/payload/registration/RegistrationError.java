package io.biza.heimdall.payload.registration;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.heimdall.payload.enumerations.RegistrationErrorType;
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
public class RegistrationError {

  @JsonProperty("error")
  @NotEmpty
  @Schema(description = "Predefined error code as described in section 3.3 OIDC Dynamic Client Registration")
  RegistrationErrorType error;
  
  @JsonProperty("error_description")
  @NotEmpty
  @Schema(description = "Additional text description of the error for debugging.")
  String errorDescription;

}

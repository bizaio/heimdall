package io.biza.wikkitgate.shared.cdr.payloads;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Schema(description = "Dynamic Registration Request with CDR Extensions")
public class CDRRegistrationRequest {
  
  @JsonProperty("iss")
  @NotEmpty
  @Schema(
      description = "ADR Software Product Identier as defined by the CDR Register")
  String iss;

}

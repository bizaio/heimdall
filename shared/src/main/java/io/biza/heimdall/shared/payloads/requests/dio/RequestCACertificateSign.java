package io.biza.heimdall.shared.payloads.requests.dio;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.heimdall.payload.enumerations.CertificateType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Valid
@ToString
@EqualsAndHashCode
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to generate one or more Register JWKs")
public class RequestCACertificateSign {

  @JsonProperty("validity")
  @NotNull
  @Schema(description = "How many years to sign the certificate for", defaultValue = "1")
  @Min(1)
  @Builder.Default
  Integer validity = 1;
  
  @JsonProperty("certificateType")
  @NotNull
  CertificateType certificateType;
  
  @JsonProperty("commonName")
  @NotNull
  String commonName;
  
  
}

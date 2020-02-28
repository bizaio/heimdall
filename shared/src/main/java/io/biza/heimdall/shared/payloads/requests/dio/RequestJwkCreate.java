package io.biza.heimdall.shared.payloads.requests.dio;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RequestJwkCreate {

  @JsonProperty("keyCount")
  @NotNull
  @NonNull
  @Schema(description = "How many keys to produce", defaultValue = "1")
  @Builder.Default
  Integer keyCount = 1;
  
}

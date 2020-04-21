package io.biza.heimdall.shared.payloads.dio;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
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
@Schema(
    description = "The data that is common to all organisations, regardless of the type (e.g. company, trust, partnership, government)")
public class DioDataHolder {

  @JsonProperty("id")
  @Schema(description = "Data Holder Identifier", accessMode = AccessMode.READ_ONLY)
  UUID id;

  @JsonProperty("name")
  @NotEmpty
  @Schema(description = "Unique legal name of the organisation")
  String name;

  @JsonProperty("legalEntity")
  @NotNull
  @Schema(description = "Legal Entity details for Data Holder")
  DioLegalEntity legalEntity;

  @JsonProperty("industry")
  @NotNull
  IndustryType industry;

}

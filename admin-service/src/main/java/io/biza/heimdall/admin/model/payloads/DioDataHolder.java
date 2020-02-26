package io.biza.heimdall.admin.model.payloads;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.CountryStringToLocaleConverter;
import io.biza.babelfish.cdr.converters.LocalDateToStringConverter;
import io.biza.babelfish.cdr.converters.LocaleToCountryStringConverter;
import io.biza.babelfish.cdr.converters.StringToLocalDateConverter;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.heimdall.payload.enumerations.IndustryType;
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
@Schema(description = "The data that is common to all organisations, regardless of the type (e.g. company, trust, partnership, government)")
public class DioDataHolder {
  
  @JsonProperty("id")
  @NotNull
  @Schema(description = "Data Holder Identifier")
  UUID id;
  
  @JsonProperty("name")
  @NotEmpty
  @Schema(
      description = "Unique legal name of the organisation")
  String name;
  
  @JsonProperty("legalEntity")
  @NotNull
  @Schema(description = "Legal Entity details for Data Holder")
  DioLegalEntity legalEntity;
  
  @JsonProperty("industry")
  @NotNull
  IndustryType industry;

}

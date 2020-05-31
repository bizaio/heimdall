package io.biza.heimdall.shared.payloads.dio;

import java.time.LocalDate;
import java.util.Locale;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.babelfish.common.jackson.CountryStringToLocaleConverter;
import io.biza.babelfish.common.jackson.LocalDateToStringConverter;
import io.biza.babelfish.common.jackson.LocaleToCountryStringConverter;
import io.biza.babelfish.common.jackson.StringToLocalDateConverter;
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
@Schema(
    description = "The data that is common to all organisations, regardless of the type (e.g. company, trust, partnership, government)")
public class DioLegalEntity {

  @JsonProperty("name")
  @Schema(description = "Unique legal name of the organisation")
  String name;

  @JsonProperty("registrationNumber")
  @Schema(
      description = "Unique registration number (if the company is registered outside Australia)")
  String registrationNumber;

  @JsonProperty("registrationDate")
  @JsonSerialize(converter = LocalDateToStringConverter.class)
  @JsonDeserialize(converter = StringToLocalDateConverter.class)
  @Schema(description = "Date of registration (if the company is registered outside Australia)",
      type = "string", format = "date")
  LocalDate registrationDate;

  @Schema(
      description = "Enumeration with values from [ISO 3166 Alpha-3](https://www.iso.org/iso-3166-country-codes.html) country codes.  Assumed to be AUS if absent",
      type = "string")
  @JsonSerialize(converter = LocaleToCountryStringConverter.class)
  @JsonDeserialize(converter = CountryStringToLocaleConverter.class)
  @JsonProperty("registeredCountry")
  Locale registeredCountry;

  @Schema(description = "Australian Business Number for the organisation")
  @JsonProperty("abn")
  String abn;

  @Schema(
      description = "Australian Company Number for the organisation. Required only if an ACN is applicable for the organisation type")
  @JsonProperty("acn")
  String acn;

  @Schema(
      description = "Australian Registered Body Number. ARBNs are issued to registrable Australian bodies and foreign companies")
  @JsonProperty("arbn")
  String arbn;

  @JsonProperty("industryCode")
  @Schema(description = "Industry Type")
  String industryCode;

  @Schema(description = "Legal organisation type")
  @JsonProperty("organisationType")
  @Valid
  CommonOrganisationType organisationType;

}

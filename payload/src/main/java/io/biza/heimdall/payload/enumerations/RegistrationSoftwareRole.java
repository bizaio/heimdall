package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register Software Role", enumAsRef = true)
public enum RegistrationSoftwareRole implements LabelValueEnumInterface {
  // @formatter:off
  DATA_RECIPIENT_SOFTWARE_PRODUCT("DATA_RECIPIENT_SOFTWARE_PRODUCT", "data-recipient-software-product");
  // @formatter:on

  private String value;

  private String label;

  RegistrationSoftwareRole(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RegistrationSoftwareRole fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (RegistrationSoftwareRole b : RegistrationSoftwareRole.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of RegistrationSoftwareRole from " + text,
        RegistrationSoftwareRole.class.getSimpleName(), RegistrationSoftwareRole.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register Software Role", enumAsRef = true)
public enum RegisterSoftwareRole implements LabelValueEnumInterface {
  // @formatter:off
  DATA_RECIPIENT_SOFTWARE_PRODUCT("data-recipient-software-product", "data-recipient-software-product");
  // @formatter:on

  private String value;

  private String label;

  RegisterSoftwareRole(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RegisterSoftwareRole fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (RegisterSoftwareRole b : RegisterSoftwareRole.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of DataHolderStatusType from " + text,
        RegisterSoftwareRole.class.getSimpleName(), RegisterSoftwareRole.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
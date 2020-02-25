package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register Auth Configuration Type", enumAsRef = true)
public enum RegisterAuthType implements LabelValueEnumInterface {
  // @formatter:off
  HYBRIDFLOW_JWKS("HYBRIDFLOW-JWKS", "Hybrid Flow with JWKS");
  // @formatter:on

  private String value;

  private String label;

  RegisterAuthType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RegisterAuthType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (RegisterAuthType b : RegisterAuthType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of RegisterAuthType from " + text,
        RegisterAuthType.class.getSimpleName(), RegisterAuthType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
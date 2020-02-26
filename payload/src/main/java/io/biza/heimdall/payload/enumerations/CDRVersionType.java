package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "CDR Major Version Type", enumAsRef = true)
public enum CDRVersionType implements LabelValueEnumInterface {
  // @formatter:off
  V1("V1", "V1");
  // @formatter:on

  private String value;

  private String label;

  CDRVersionType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CDRVersionType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (CDRVersionType b : CDRVersionType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of CDRVersionType from " + text,
        CDRVersionType.class.getSimpleName(), CDRVersionType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
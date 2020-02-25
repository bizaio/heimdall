package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "CDR Holder Status", enumAsRef = true)
public enum SoftwareProductStatusType implements LabelValueEnumInterface {
  // @formatter:off
  ACTIVE("ACTIVE", "Active"),
  INACTIVE("INACTIVE", "Inactive"),
  REMOVED("REMOVED", "Removed");
  // @formatter:on

  private String value;

  private String label;

  SoftwareProductStatusType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SoftwareProductStatusType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (SoftwareProductStatusType b : SoftwareProductStatusType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of SoftwareProductStatusType from " + text,
        SoftwareProductStatusType.class.getSimpleName(), SoftwareProductStatusType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Recipient Status", enumAsRef = true)
public enum DataRecipientStatusType implements LabelValueEnumInterface {
  // @formatter:off
  ACTIVE("ACTIVE", "Active"),
  SUSPENDED("SUSPENDED", "Suspended"),
  REVOKED("REVOKED", "Revoked"),
  SURRENDERED("SURRENDERED", "Surrendered");
  // @formatter:on

  private String value;

  private String label;

  DataRecipientStatusType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DataRecipientStatusType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (DataRecipientStatusType b : DataRecipientStatusType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of SoftwareProductStatusType from " + text,
        DataRecipientStatusType.class.getSimpleName(), DataRecipientStatusType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
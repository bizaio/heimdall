package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Certificate Type", enumAsRef = true)
public enum CertificateType implements LabelValueEnumInterface {
  // @formatter:off
  CLIENT("CLIENT", "Client Certificate"),
  SERVER("SERVER", "Server Certificate");
  // @formatter:on

  private String value;

  private String label;

  CertificateType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CertificateType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (CertificateType b : CertificateType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of CertificateStatus from " + text,
        CertificateType.class.getSimpleName(), CertificateType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
package io.biza.heimdall.auth.exceptions;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class CryptoException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public JoseException jose;
  public InvalidJwtException invalidJwt;
  
}

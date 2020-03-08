package io.biza.heimdall.shared.util;

import java.util.Map.Entry;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import io.biza.babelfish.oidc.payloads.JWTClaims;

public class JoseClaimsUtil {
  
  public static JwtClaims toJwtClaims(JWTClaims claims) {
    JwtClaims jwt = new JwtClaims();
    if(claims.issuer() != null) {
      jwt.setIssuer(claims.issuer());
    }
    
    if(claims.subject() != null) {
    jwt.setSubject(claims.subject());
    }
    
    if(claims.audience() != null) {
    jwt.setAudience(claims.audience());
    }
    
    if(claims.expiry() != null) {
    jwt.setExpirationTime(NumericDate.fromSeconds(claims.expiry().toEpochSecond()));
    }
    
    if(claims.notBefore() != null) {
      jwt.setNotBefore(NumericDate.fromSeconds(claims.notBefore().toEpochSecond()));
    }
    
    if(claims.issuedAt() != null) {
      jwt.setIssuedAt(NumericDate.fromSeconds(claims.issuedAt().toEpochSecond()));
    }
    
    if(claims.jti() != null) {
      jwt.setJwtId(claims.jti());
    } else {
      jwt.setGeneratedJwtId();
    }
    
    if(claims.additionalClaims() != null) {
      for(Entry<String, Object> claim : claims.additionalClaims().entrySet()) {
        jwt.setClaim(claim.getKey(), claim.getValue());
      }
    }
    
    return jwt;
  }
}

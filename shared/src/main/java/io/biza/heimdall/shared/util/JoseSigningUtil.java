package io.biza.heimdall.shared.util;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.VerificationJwkSelector;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
@Valid
public class JoseSigningUtil {

  public static final String[] SIGNING_ALGORITHMS = {AlgorithmIdentifiers.RSA_PSS_USING_SHA256};
  public static final String[] ENCRYPTION_ALGORITHMS = { KeyManagementAlgorithmIdentifiers.RSA_OAEP };
  public static final String[] ENCRYPTION_METHOD = { ContentEncryptionAlgorithmIdentifiers.AES_256_GCM };

  /**
   * Given a String performs a JWS verification and provides the result as JwtClaims
   * 
   * @param compactSerialisation A String that contains an encoded JWT
   * @param jwksUri Location to retrieve JWKS from
   * @param issuer Expected Issuer
   * @param audience Expected Audience
   * @param okHttpClient Custom OkHttpClient to use
   * @return JwtClaims containing verified claims
   * @throws IOException
   * @throws InterruptedException
   * @throws JoseException
   * @throws InvalidJwtException
   */
  public static JwtClaims verify(@NotEmpty String compactSerialisation, @NotNull URI jwksUri,
      @NotEmpty String issuer, @NotEmpty String audience, @NotNull OkHttpClient okHttpClient)
      throws IOException, InterruptedException, JoseException, InvalidJwtException {
    return verifyInternal(compactSerialisation, retrieveJwks(jwksUri, okHttpClient), issuer,
        audience, true);
  }

  /**
   * Given a String performs a JWS verification and provides the result as JwtClaims
   * 
   * @param compactSerialisation A String that contains an encoded JWT
   * @param jwksUri Location to retrieve JWKS from
   * @param issuer Expected Issuer
   * @param audience Expected Audience
   * @return JwtClaims containing verified claims
   * @throws IOException
   * @throws InterruptedException
   * @throws JoseException
   * @throws InvalidJwtException
   */
  public static JwtClaims verify(@NotEmpty String compactSerialisation, @NotNull URI jwksUri,
      @NotEmpty String issuer, @NotEmpty String audience)
      throws JoseException, IOException, InvalidJwtException, InterruptedException {
    return verify(compactSerialisation, jwksUri, issuer, audience, new OkHttpClient());
  }

  /**
   * Given a String performs a JWS verification but doesn't enforce expected audience
   * 
   * @param compactSerialisation A String that contains an encoded JWT
   * @param jwksUri Location to retrieve JWKS from
   * @param issuer Expected Issuer
   * @return JwtClaims containing verified claims
   * @throws IOException
   * @throws InterruptedException
   * @throws JoseException
   * @throws InvalidJwtException
   */
  public static JwtClaims verifyNoAudience(@NotEmpty String compactSerialisation,
      @NotNull URI jwksUri, @NotEmpty String issuer)
      throws JoseException, IOException, InvalidJwtException, InterruptedException {
    return verifyInternal(compactSerialisation, retrieveJwks(jwksUri, new OkHttpClient()), issuer,
        null, true);
  }

  /**
   * Given a String performs a JWS verification but doesn't enforce subject
   * 
   * @param compactSerialisation A String that contains an encoded JWT
   * @param jwksUri Location to retrieve JWKS from
   * @param issuer Expected Issuer
   * @param audience Expected audience
   * @return JwtClaims containing verified claims
   * @throws IOException
   * @throws InterruptedException
   * @throws JoseException
   * @throws InvalidJwtException
   */
  public static JwtClaims verifyNoSubject(@NotEmpty String compactSerialisation,
      @NotNull URI jwksUri, @NotEmpty String issuer, @NotEmpty String audience)
      throws JoseException, IOException, InvalidJwtException, InterruptedException {
    return verifyInternal(compactSerialisation, retrieveJwks(jwksUri, new OkHttpClient()), issuer,
        audience, false);
  }


  private static JsonWebKeySet retrieveJwks(@NotNull URI jwksUri,
      @NotNull OkHttpClient okHttpClient) throws JoseException, IOException {
    Request request = new Request.Builder().url(jwksUri.toURL())
        .addHeader("User-Agent", "Biza.io Babelfish OIDC").build();

    Response response = okHttpClient.newCall(request).execute();

    if (response.isSuccessful()) {
      return new JsonWebKeySet(response.body().string());
    } else {
      throw new IOException("Received error code " + response.code()
          + " during loading of JWKS from " + jwksUri.toString());
    }

  }


  /**
   * Private method which does the actual work after initialisation components have completed
   * 
   * @param compactSerialisation A String that contains an encoded JWT
   * @param jwks A set of Json Web Key's as a JsonWebKeySet
   * @param issuer Expected Issuer
   * @param audience Expected Audience
   * @return JwtClaims containing verified claims
   * @throws IOException
   * @throws InterruptedException
   * @throws JoseException
   * @throws InvalidJwtException
   */
  private static JwtClaims verifyInternal(String compactSerialization, JsonWebKeySet jwks,
      String issuer, String audience, Boolean requireSubject)
      throws JoseException, IOException, InvalidJwtException {

    LOG.debug("Attempting to verify and deserialise claims value {} into JwtClaims",
        compactSerialization);

    JsonWebSignature jws = new JsonWebSignature();
    jws.setCompactSerialization(compactSerialization);

    VerificationJwkSelector jwkSelector = new VerificationJwkSelector();
    JsonWebKey jwk = jwkSelector.select(jws, jwks.getJsonWebKeys());

    if (jwk == null) {
      throw new IOException("Unable to identify JWK from supplied JWKS, aborting");
    }

    LOG.debug(
        "Supplied public key from jwks is: \n-----BEGIN PUBLIC KEY-----\n{}\n-----END PUBLIC KEY-----\n",
        Base64.getEncoder().encodeToString(jwk.getKey().getEncoded()).replaceAll(".{80}(?=.)",
            "$0\n"));

    JwtConsumerBuilder jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime()
        .setAllowedClockSkewInSeconds(30).setExpectedIssuer(issuer).setVerificationKey(jwk.getKey())
        .setJwsAlgorithmConstraints(ConstraintType.WHITELIST, SIGNING_ALGORITHMS);

    /**
     * Optionally check audience (this is a private method so decision to enforce is upstream)
     */
    if (audience != null) {
      jwtConsumer.setExpectedAudience(audience);
    }

    /**
     * Optionally check subject is defined
     */
    if (requireSubject) {
      jwtConsumer.setRequireSubject();
    }

    return jwtConsumer.build().processToClaims(compactSerialization);

  }

  /**
   * Given a set of claims and a Web Key containing a Private Key performs key signing and returns
   * the content as an encoded String
   * 
   * @param claims A set of JWT Claims to perform signing on
   * @param key The key to sign content with
   * @return String containing a Signed JWT
   * @throws IOException
   * @throws JoseException
   */
  public static String sign(JwtClaims claims, PublicJsonWebKey key)
      throws IOException, JoseException {

    if (key == null || key.getPrivateKey() == null) {
      throw new IOException(
          "Signing Key Pair is not initialised or does not contain a private key so signing of claims is not possible");
    }

    LOG.debug("Attempting to sign claims value {} into a signed string", claims);
    LOG.debug("Signing using public key detail of {}",
        Base64.getEncoder().encodeToString(key.getPublicKey().getEncoded()));

    JsonWebSignature jws = new JsonWebSignature();

    jws.setHeader("typ", "JWT");
    jws.setDoKeyValidation(true);
    jws.setPayload(claims.toJson());
    jws.setAlgorithmHeaderValue(SIGNING_ALGORITHMS[0]);
    jws.setKey(key.getPrivateKey());
    jws.setKeyIdHeaderValue(key.getKeyId());

    return jws.getCompactSerialization();

  }
  
  /**
   * Given a set of claims, a sender key and a receiver key sign then encrypt contents
   * into a nested jwt (aka JOSE)
   * 
   * @param payload a String payload to encrypt
   * @param key The key to encrypto to
   * @return String containing an Encrypted Payload
   * @throws IOException
   * @throws JoseException
   */
  public static String encrypt(JwtClaims claims, PublicJsonWebKey senderKey, PublicJsonWebKey receiverKey)
      throws IOException, JoseException {

    if (senderKey == null || senderKey.getPrivateKey() == null) {
      throw new IOException(
          "Signing Key Pair is not initialised or does not contain a private key so signing of claims is not possible");
    }
    
    if (receiverKey == null || receiverKey.getPublicKey() == null) {
      throw new IOException(
          "Receiving Public Key is not initialised or doesn't contain a public key");
    }

    /**
     * First sign it
     */
    String signedJwt = sign(claims, senderKey);
    
    /**
     * Now encrypt it
     */
    JsonWebEncryption jwe = new JsonWebEncryption();
    jwe.setAlgorithmHeaderValue(ENCRYPTION_ALGORITHMS[0]);
    jwe.setEncryptionMethodHeaderParameter(ENCRYPTION_METHOD[0]);
    
    /**
     * Need to send it somewhere
     */
    jwe.setKey(receiverKey.getPublicKey());
    jwe.setKeyIdHeaderValue(receiverKey.getKeyId());
    
    /**
     * Signal this is a nested JWT
     */
    jwe.setContentTypeHeaderValue("JWT");
    
    /**
     * Wire the goods in
     */
    jwe.setPayload(signedJwt);
    
    /**
     * SHOW TIME!
     */
    return jwe.getCompactSerialization();

  }
}

/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *******************************************************************************/
package io.biza.heimdall.auth.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import io.biza.babelfish.oidc.enumerations.OAuth2ErrorCode;
import io.biza.babelfish.oidc.requests.OAuth2ErrorResponse;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidGrantException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.UnauthorisedClientException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;
import io.biza.heimdall.auth.Constants;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionController {
  
  @ExceptionHandler(InvalidClientException.class)
  public ResponseEntity<Object> handleInvalidClientException(HttpServletRequest req,
      InvalidClientException ex) {

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.INVALID_CLIENT)
            .errorDescription(Constants.OAUTH2_INVALID_CLIENT_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }

  @ExceptionHandler(CryptoException.class)
  public ResponseEntity<Object> handleInvalidClientException(HttpServletRequest req,
      CryptoException ex) {

    if (ex.invalidJwt() != null) {
      return ResponseEntity.badRequest()
          .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.INVALID_GRANT)
              .errorDescription(ex.invalidJwt().getMessage())
              .errorUri(Constants.OAUTH2_INVALID_JWT_RESPONSE_URI).build());

    } else {
      return ResponseEntity.badRequest()
          .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.SERVER_ERROR)
              .errorDescription(Constants.OAUTH2_SERVER_ERROR_MESSAGE).build());
    }
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Object> handleInvalidGrantException(HttpServletRequest req,
      NullPointerException ex) {

    LOG.error("Encountered uncaught NullPointerException", ex);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
  }

  @ExceptionHandler(InvalidGrantException.class)
  public ResponseEntity<Object> handleInvalidGrantException(HttpServletRequest req,
      InvalidGrantException ex) {

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.INVALID_GRANT)
            .errorDescription(Constants.OAUTH2_INVALID_GRANT_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<Object> handleInvalidRequestException(HttpServletRequest req,
      InvalidRequestException ex) {

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.INVALID_REQUEST)
            .errorDescription(Constants.OAUTH2_INVALID_REQUEST_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Object> handleInvalidMediaType(HttpServletRequest req,
      HttpMediaTypeNotSupportedException ex) {

    LOG.error("Received unsupported media type exception", ex);

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.INVALID_REQUEST)
            .errorDescription(Constants.OAUTH2_INVALID_REQUEST_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }

  @ExceptionHandler(InvalidScopeException.class)
  public ResponseEntity<Object> handleInvalidScopeException(HttpServletRequest req,
      InvalidScopeException ex) {

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.INVALID_CLIENT)
            .errorDescription(Constants.OAUTH2_INVALID_SCOPE_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }

  @ExceptionHandler(UnauthorisedClientException.class)
  public ResponseEntity<Object> handleUnauthorisedClientException(HttpServletRequest req,
      UnauthorisedClientException ex) {

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.UNAUTHORISED_CLIENT)
            .errorDescription(Constants.OAUTH2_UNAUTHORIZED_CLIENT_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }


  @ExceptionHandler(UnsupportedGrantTypeException.class)
  public ResponseEntity<Object> handleUnsupportedGrantTypeException(HttpServletRequest req,
      UnsupportedGrantTypeException ex) {

    return ResponseEntity.badRequest()
        .body(OAuth2ErrorResponse.builder().error(OAuth2ErrorCode.UNSUPPORTED_GRANT_TYPE)
            .errorDescription(Constants.OAUTH2_UNSUPPORTED_GRANT_TYPE_MESSAGE)
            .errorUri(Constants.OAUTH2_ERROR_RESPONSE_URI).build());
  }


}

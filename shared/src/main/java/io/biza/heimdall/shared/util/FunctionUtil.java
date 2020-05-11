package io.biza.heimdall.shared.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import io.biza.babelfish.cdr.models.responses.ResponseValidationError;
import io.biza.babelfish.common.enumerations.BabelExceptionType;
import io.biza.babelfish.common.enumerations.BabelValidationErrorType;
import io.biza.babelfish.common.exceptions.NotFoundException;
import io.biza.babelfish.common.exceptions.ValidationListException;
import io.biza.babelfish.common.payloads.BabelValidationError;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FunctionUtil {

  public static <T> Message<T> ok(T payload) {
    return MessageBuilder.withPayload(payload)
        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.OK).build();
  }

  public static Message<Object> noContent() {
    return MessageBuilder.withPayload(new Object())
        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.NO_CONTENT).build();
  }

  public static <T> Message<?> notFound() {
    return MessageBuilder.withPayload(new Object())
        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.NOT_FOUND).build();
  }


  public static <T> Message<?> notFound(NotFoundException e) {
    LOG.info("Encountered NotFoundException with message of: {}", e.message());
    return MessageBuilder.withPayload(new Object())
        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.NOT_FOUND).build();
  }

  public static <T> Message<?> unavailable(Exception e) {
    LOG.info("Encountered error leading to service unavailability with message of: {}",
        e.getMessage());
    return MessageBuilder.withPayload(new Object())
        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.SERVICE_UNAVAILABLE).build();
  }

  public static <T> Message<T> notAcceptable(T payload) {
    return MessageBuilder.withPayload(payload)
        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setHeader(HttpHeaders.STATUS_CODE, HttpStatus.NOT_ACCEPTABLE).build();
  }



  public static <T> Message<ResponseValidationError> validationErrors(
      Set<ConstraintViolation<T>> violations, BabelExceptionType type, String explanation) {

    List<BabelValidationError> validationErrors = new ArrayList<BabelValidationError>();

    if (violations != null) {
      for (ConstraintViolation<T> violation : violations) {
        validationErrors
            .add(BabelValidationError.builder().fields(List.of(violation.getPropertyPath().toString()))
                .message(StringUtils.capitalize(violation.getMessage()))
                .type(BabelValidationErrorType.ATTRIBUTE_INVALID).build());
      }
    }

    return notAcceptable(ResponseValidationError.builder().type(type).explanation(explanation)
        .validationErrors(validationErrors).build());
  }

  public static Message<?> validationErrors(ValidationListException e) {
    return notAcceptable(ResponseValidationError.builder().type(e.type())
        .explanation(e.explanation()).validationErrors(e.validationErrors()).build());
  }
}

package io.biza.heimdall.shared.component.support;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.util.ValidationUtil;

@Service
public class ValidationService {

  @Autowired
  Validator validator;

  public <T> void validate(T data, String errorMessage) throws ValidationListException {
    Set<ConstraintViolation<T>> validationErrors = validator.validate(data);

    if (!validationErrors.isEmpty()) {
      throw ValidationListException.builder().explanation(errorMessage)
          .type(HeimdallExceptionType.VALIDATION_ERROR)
          .validationErrors(ValidationUtil.toValidationList(validationErrors)).build();
    }
  }

}

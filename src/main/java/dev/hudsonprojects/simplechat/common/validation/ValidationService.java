package dev.hudsonprojects.simplechat.common.validation;


import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import dev.hudsonprojects.simplechat.common.exception.ValidationException;
import dev.hudsonprojects.simplechat.common.lib.validation.ValidationUtils;
import dev.hudsonprojects.simplechat.common.messages.builder.ErrorDetailsBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class ValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationService.class.getName());

	@Autowired
	private Validator validator;

	public <T> void applyValidation(T object, Class<?>... groups){
		Errors errors = getErrors(object, groups);
		if(errors.hasFieldErrors()) {
			throw new ValidationException(ErrorDetailsBuilder.withFieldErrors(errors.getFieldErrors()).build());
		}
	}
	
	private Errors getErrors(Object object, Class<?>... groups) {
		Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
		Set<ConstraintViolation<Object>> validations = validator.validate(object, groups);
		for (ConstraintViolation<Object> violation : validations) {
			String field = violation.getPropertyPath().toString();
			FieldError fieldError = errors.getFieldError(field);
			if (fieldError == null || !fieldError.isBindingFailure()) {
				try {
					errors.rejectValue(field,
							violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
							ValidationUtils.getArgumentsForConstraint(validator, errors.getObjectName(), field,
									violation.getConstraintDescriptor()),
							violation.getMessage());
				} catch (NotReadablePropertyException e) {
					LOGGER.error("JSR-303 validated property '" + field
							+ "' does not have a corresponding accessor for Spring data binding - "
							+ "check your DataBinder's configuration (bean property versus direct field access)", e);
				}
			}
		}
		return errors;
	}

}

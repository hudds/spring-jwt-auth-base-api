package dev.hudsonprojects.simplechat.common.lib.validation;

import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path.Node;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.Validator;

public interface ValidationUtils {

	static String getFieldName(ConstraintViolation<?> violation) {
		String fieldName = null;
		for (Node node : violation.getPropertyPath()) {
			fieldName = node.toString();
		}
		return fieldName;
	}

	public static <T> String format(ConstraintViolation<T> v) {
		return v.getMessageTemplate();
	}
	
	public static Object[] getArgumentsForConstraint(Validator validator, String objectName, String field, ConstraintDescriptor<?> descriptor) {
		return new GetArgumentsForConstraint(validator).get(objectName, field, descriptor);
	}

}

class GetArgumentsForConstraint extends SpringValidatorAdapter{

	GetArgumentsForConstraint(Validator validator) {
		super(validator);
	}

	public Object[] get(String objectName, String field, ConstraintDescriptor<?> descriptor) {
		return super.getArgumentsForConstraint(objectName, field, descriptor);
	}
}

package dev.hudsonprojects.simplechat.common.messages.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import dev.hudsonprojects.simplechat.common.builder.Builder;
import dev.hudsonprojects.simplechat.common.exception.APIErrorType;
import dev.hudsonprojects.simplechat.common.messages.APIMessage;
import dev.hudsonprojects.simplechat.common.messages.ErrorDetails;

public class ErrorDetailsBuilder implements Builder<ErrorDetails> {
	
	private final ErrorDetails errorDetails;
	
	private ErrorDetailsBuilder() {
		this.errorDetails =  new ErrorDetails();
	}
	
	public ErrorDetailsBuilder setType(APIErrorType type) {
		this.errorDetails.setType(type);
		return this;
	}
		
	public ErrorDetailsBuilder setMessage(APIMessage message) {
		this.errorDetails.setMessage(message);
		return this;
	}
	public ErrorDetailsBuilder setMessage(String message, Object ... args) {
		this.errorDetails.setMessage(new APIMessage(message, args));
		return this;
	}
	
	public ErrorDetailsBuilder setStatus(HttpStatus status) {
		this.errorDetails.setStatus(status);
		return this;
	}
	
	public ErrorDetailsBuilder setFieldErrors(List<APIFieldError> fieldErrors) {
		this.errorDetails.setFieldErrors(fieldErrors);
		return this;
	}
	
	
	public ErrorDetailsBuilder addFieldError(String field, APIMessage ... errors) {
		initFieldErrorsIfAbsent();
		for(var error : errors) {
			this.errorDetails.addFieldError(new APIFieldError(field, Collections.singletonList(error)));
		}
		return this;
	}

	public ErrorDetailsBuilder addFieldError(String field, String... errors) {
		initFieldErrorsIfAbsent();
		this.errorDetails.addFieldError(new APIFieldError(field, Stream.of(errors).map(APIMessage::new).toList()));
		return this;
	}
	
	
	public ErrorDetailsBuilder addFieldError(String field, Collection<String> errors) {
		initFieldErrorsIfAbsent();
		this.errorDetails.addFieldError(new APIFieldError(field, errors.stream().map(APIMessage::new).toList()));
		return this;
	}
	

	public ErrorDetailsBuilder addFieldErrors(Collection<FieldError> fieldErrors) {
		for(FieldError fieldError : fieldErrors) {
			addFieldError(fieldError.getField(), new APIMessage(fieldError.getDefaultMessage(), fieldError.getArguments()));	
		}
		return this;
	}
	
	@Override
	public ErrorDetails build() {
		return this.errorDetails;
	}
	
	private void initFieldErrorsIfAbsent() {
		if(this.errorDetails.getFieldErrors() == null) {
			this.errorDetails.setFieldErrors(new ArrayList<>());
		}
	}
	
	public static ErrorDetailsBuilder builder() {
		return new ErrorDetailsBuilder();
	}
	
	public static ErrorDetailsBuilder withFieldError(String field, String code, Object ... args) {
		return builder().addFieldError(field, new APIMessage(code, args));	
	}
	
	public static ErrorDetails buildWithFieldError(String field, String code, Object ... args) {
		return withFieldError(field, code, args).build();	
	}
	
	public static ErrorDetailsBuilder withMessage(String code, Object ... args) {
		return builder().setMessage(new APIMessage(code, args));	
	}
	
	public static ErrorDetailsBuilder validationError() {
		return builder().setType(APIErrorType.VALIDATION_ERROR);
	}
	
	public static ErrorDetailsBuilder unauthorized() {
		return builder().setType(APIErrorType.UNAUTHORIZED);
	}
	
	public static ErrorDetailsBuilder withFieldErrors(Collection<FieldError> fieldErrors) {
		return builder().addFieldErrors(fieldErrors);
	}

	public static ErrorDetails buildWithMessage(String code, Object ... args) {
		return withMessage(code, args).build();	
	}
	
	
}

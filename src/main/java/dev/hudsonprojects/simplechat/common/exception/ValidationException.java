package dev.hudsonprojects.simplechat.common.exception;

import dev.hudsonprojects.simplechat.common.messages.ErrorDetails;

public class ValidationException extends APIMessageException {

	private static final long serialVersionUID = 1L;

	public ValidationException(ErrorDetails errorDetails) {
		super(errorDetails, APIErrorType.VALIDATION_ERROR);
	}

	public ValidationException(ErrorDetails errorDetails, String message) {
		super(errorDetails, APIErrorType.VALIDATION_ERROR, message);
	}

	public ValidationException(ErrorDetails errorDetails, String message, Throwable cause) {
		super(errorDetails, APIErrorType.VALIDATION_ERROR, message, cause);
	}

	public ValidationException(ErrorDetails errorDetails, Throwable cause) {
		super(errorDetails, APIErrorType.VALIDATION_ERROR, cause);
	}

}

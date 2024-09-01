package dev.hudsonprojects.simplechat.common.exception;

import dev.hudsonprojects.simplechat.common.messages.ErrorDetails;

public class UnauthorizedException extends APIMessageException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException(ErrorDetails errorDetails) {
		super(errorDetails, APIErrorType.UNAUTHORIZED);
	}

	public UnauthorizedException(ErrorDetails errorDetails, String message) {
		super(errorDetails, APIErrorType.UNAUTHORIZED, message);
	}

	public UnauthorizedException(ErrorDetails errorDetails, String message, Throwable cause) {
		super(errorDetails, APIErrorType.UNAUTHORIZED, message, cause);
	}

	public UnauthorizedException(ErrorDetails errorDetails, Throwable cause) {
		super(errorDetails, APIErrorType.UNAUTHORIZED, cause);
	}

}

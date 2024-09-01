package dev.hudsonprojects.simplechat.common.exception;

import dev.hudsonprojects.simplechat.common.messages.ErrorDetails;

public class NotFoundException extends APIMessageException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(ErrorDetails rrrorDetails) {
		super(rrrorDetails, APIErrorType.NOT_FOUND);
	}

	public NotFoundException(ErrorDetails rrrorDetails, String message) {
		super(rrrorDetails, APIErrorType.NOT_FOUND, message);
	}

	public NotFoundException(ErrorDetails rrrorDetails, String message, Throwable cause) {
		super(rrrorDetails, APIErrorType.NOT_FOUND, message, cause);
	}

	public NotFoundException(ErrorDetails rrrorDetails, Throwable cause) {
		super(rrrorDetails, APIErrorType.NOT_FOUND, cause);
	}

}

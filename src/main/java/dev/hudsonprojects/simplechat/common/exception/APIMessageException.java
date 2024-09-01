package dev.hudsonprojects.simplechat.common.exception;

import java.util.Optional;

import dev.hudsonprojects.simplechat.common.messages.ErrorDetails;

public class APIMessageException extends RuntimeException {

	private static final long serialVersionUID = -6720766860486835586L;
	
	private final ErrorDetails errorDetails;
	private final APIErrorType type;
	
	
	public APIMessageException(ErrorDetails errorDetails, APIErrorType type) {
		this.errorDetails = errorDetails;
		this.type = type;
		getErrorDetails().ifPresent(e -> e.setType(getType()));
	}
	
	public APIMessageException(ErrorDetails errorDetails, APIErrorType type, Throwable cause) {
		super(cause);
		this.errorDetails = errorDetails;
		this.type = type;
		getErrorDetails().ifPresent(e -> e.setType(getType()));
	}
	
	public APIMessageException(ErrorDetails errorDetails, APIErrorType type, String exceptionMessage, Throwable cause) {
		super(exceptionMessage, cause);
		this.errorDetails = errorDetails;
		this.type = type;
		getErrorDetails().ifPresent(e -> e.setType(getType()));
	}
	
	public APIMessageException(ErrorDetails errorDetails, APIErrorType type, String exceptionMessage) {
		super(exceptionMessage);
		this.errorDetails = errorDetails;
		this.type = type;
		getErrorDetails().ifPresent(e -> e.setType(getType()));
	}
	
	public APIMessageException(APIErrorType type, String exceptionMessage, Throwable cause) {
		super(exceptionMessage, cause);
		this.errorDetails = null;
		this.type = type;
		getErrorDetails().ifPresent(e -> e.setType(getType()));
	}
	
	public APIMessageException(APIErrorType type, String exceptionMessage) {
		super(exceptionMessage);
		this.errorDetails = null;
		this.type = type;
		getErrorDetails().ifPresent(e -> e.setType(getType()));
	}

	public APIErrorType getType() {
		return type;
	}


	public Optional<ErrorDetails> getErrorDetails() {
		return Optional.ofNullable(this.errorDetails);
	}
	

}

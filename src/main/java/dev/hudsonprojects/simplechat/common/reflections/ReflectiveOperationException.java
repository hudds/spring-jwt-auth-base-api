package dev.hudsonprojects.simplechat.common.reflections;

public class ReflectiveOperationException extends RuntimeException {

	private static final long serialVersionUID = -104809031687151281L;

	public ReflectiveOperationException() {
		super();
	}

	public ReflectiveOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReflectiveOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectiveOperationException(String message) {
		super(message);
	}

	public ReflectiveOperationException(Throwable cause) {
		super(cause);
	}

	
	
}

package dev.hudsonprojects.simplechat.common.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.hudsonprojects.simplechat.common.exception.APIErrorType;
import dev.hudsonprojects.simplechat.common.exception.APIMessageException;
import dev.hudsonprojects.simplechat.common.exception.NotFoundException;
import dev.hudsonprojects.simplechat.common.exception.UnauthorizedException;
import dev.hudsonprojects.simplechat.common.exception.ValidationException;
import dev.hudsonprojects.simplechat.common.messages.ErrorDetails;
import dev.hudsonprojects.simplechat.common.messages.ErrorDetailsResolved;
import dev.hudsonprojects.simplechat.common.messages.MessageResolver;
import dev.hudsonprojects.simplechat.common.messages.builder.ErrorDetailsBuilder;




@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private MessageResolver messageResolver;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorDetailsResolved> handleValidationExcpetion(ValidationException e){
		ErrorDetails errorDetails = e.getErrorDetails().orElse(ErrorDetailsBuilder.builder().setType(e.getType()).setMessage(e.getMessage()).build());
		if(errorDetails.getStatus() == null) {
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(errorDetails.getStatus()).body(messageResolver.getResolved(errorDetails));
	}
	
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorDetailsResolved> handleNotFound(NotFoundException e){
		
		ErrorDetails errorDetails = ErrorDetailsBuilder.builder()
				.setType(e.getType())
				.setStatus(HttpStatus.NOT_FOUND)
				.setMessage(e.getMessage())
				.build();
		
		return ResponseEntity.status(errorDetails.getStatus()).body(messageResolver.getResolved(errorDetails));
	}
	
	@ExceptionHandler(APIMessageException.class)
	public ResponseEntity<ErrorDetailsResolved> handleUnauthorized(UnauthorizedException e){
		
		ErrorDetails errorDetails = e.getErrorDetails()
				.orElse(ErrorDetailsBuilder.builder()
						.setType(e.getType())
						.setMessage(e.getMessage()).build());
		
		if(errorDetails.getStatus() == null) {
			errorDetails.setStatus(HttpStatus.UNAUTHORIZED);
		}
		
		return ResponseEntity.status(errorDetails.getStatus()).body(messageResolver.getResolved(errorDetails));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetailsResolved> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

		
		ErrorDetails errorDetails = ErrorDetailsBuilder.withFieldErrors(e.getFieldErrors())
				.setStatus(HttpStatus.BAD_REQUEST)
				.setType(APIErrorType.VALIDATION_ERROR)
				.build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResolver.getResolved(errorDetails));
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorDetailsResolved> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		ErrorDetails errorDetails = ErrorDetailsBuilder.builder()
				.setStatus(HttpStatus.METHOD_NOT_ALLOWED)
				.setMessage("error.http.methodNotAllowed")
				.setType(APIErrorType.HTTP_REQUEST_ERROR)
				.build();
		
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(messageResolver.getResolved(errorDetails));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetailsResolved> handleMethodArgumentNotValidException(Exception e){
		LOGGER.error("INTERNAL SERVER ERROR", e);
		
		ErrorDetails errorDetails = ErrorDetailsBuilder.builder()
				.setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.setMessage("error.internalServerError")
				.setType(APIErrorType.INTERNAL_ERROR)
				.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResolver.getResolved(errorDetails));
	}
	
	

}




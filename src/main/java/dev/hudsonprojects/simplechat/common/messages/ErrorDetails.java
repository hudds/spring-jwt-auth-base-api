package dev.hudsonprojects.simplechat.common.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import dev.hudsonprojects.simplechat.common.exception.APIErrorType;
import dev.hudsonprojects.simplechat.common.messages.builder.APIFieldError;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDetails implements Serializable{
	
	private static final long serialVersionUID = 3739771697286260090L;
	
	private APIErrorType type;
	private APIMessage message;
	private HttpStatus status;
	private List<APIFieldError> fieldErrors;
	

	public APIErrorType getType() {
		return type;
	}
	
	public void setType(APIErrorType type) {
		this.type = type;
	}
	
	public APIMessage getMessage() {
		return message;
	}
	public void setMessage(APIMessage message) {
		this.message = message;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	public List<APIFieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<APIFieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	public void addFieldError(APIFieldError fieldError) {
		if(this.fieldErrors == null) {
			this.fieldErrors = new ArrayList<>();
		}
		if(fieldErrors.contains(fieldError)) {
			fieldErrors.stream().findFirst().ifPresent(f -> f.addMessages(fieldError.getMessages()));
			return;
		}
		fieldErrors.add(fieldError);
	}

	@Override
	public String toString() {
		return "ErrorDetails [type=" + type + ", message=" + message + ", status=" + status + ", fieldErrors="
				+ fieldErrors + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldErrors, message, status, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorDetails other = (ErrorDetails) obj;
		return Objects.equals(fieldErrors, other.fieldErrors) && Objects.equals(message, other.message)
				&& status == other.status && type == other.type;
	}
	

}


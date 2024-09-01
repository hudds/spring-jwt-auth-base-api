package dev.hudsonprojects.simplechat.common.messages.builder;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import dev.hudsonprojects.simplechat.common.messages.APIMessage;

public class APIFieldError implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private final String fieldName;
	private final Set<APIMessage> messages;
	
	public APIFieldError(String fieldName) {
		this(fieldName, new LinkedHashSet<>());
	}
	
	public APIFieldError(String fieldName, Collection<APIMessage> messages) {
		this.fieldName = fieldName;
		this.messages = messages == null ? new LinkedHashSet<>() : new LinkedHashSet<>(messages);
	}

	public String getFieldName() {
		return fieldName;
	}

	public Set<APIMessage> getMessages() {
		return messages;
	}
	
	public APIFieldError addMessage(APIMessage message) {
		this.messages.add(message);
		return this;
	}
	
	public APIFieldError addMessages(Collection<APIMessage> messages) {
		this.messages.addAll(messages);
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(fieldName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		APIFieldError other = (APIFieldError) obj;
		return Objects.equals(fieldName, other.fieldName);
	}

	@Override
	public String toString() {
		return "FieldError [fieldName=" + fieldName + ", messages=" + messages + "]";
	}
	
	

}

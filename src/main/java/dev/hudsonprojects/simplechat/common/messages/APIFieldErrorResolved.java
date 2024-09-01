package dev.hudsonprojects.simplechat.common.messages;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class APIFieldErrorResolved implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String field;
	private final Set<String> messages;
	
	public APIFieldErrorResolved(String field, Collection<String> messages) {
		this.field = field;
		this.messages = messages == null ?  Collections.emptySet() : Collections.unmodifiableSet(new LinkedHashSet<>(messages));
	}
	
	
	public String getField() {
		return field;
	}
	
	public Set<String> getMessages() {
		return new LinkedHashSet<>(messages);
	}


	@Override
	public int hashCode() {
		return Objects.hash(field, messages);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		APIFieldErrorResolved other = (APIFieldErrorResolved) obj;
		return Objects.equals(field, other.field) && Objects.equals(messages, other.messages);
	}


	@Override
	public String toString() {
		return "APIFieldErrorResolved [field=" + field + ", messages=" + messages + "]";
	}
	
}

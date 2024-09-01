package dev.hudsonprojects.simplechat.common.messages;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class APIMessage implements Serializable {
	
	private static final long serialVersionUID = -4907152397690930070L;
	
	private final String code;
	private final Object[] args;
	
	public APIMessage(String code, Object ... args) {
		this.code = code;
		this.args = Arrays.copyOf(args, args.length);
	}

	public String getCode() {
		return code;
	}
	
	public Object[] getArgs() {
		return Arrays.copyOf(args, args.length);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(args);
		result = prime * result + Objects.hash(code);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		APIMessage other = (APIMessage) obj;
		return Objects.equals(code, other.code) && Arrays.deepEquals(args, other.args);
	}

	@Override
	public String toString() {
		return "ValidationMessage [code=" + code + ", args=" + Arrays.toString(args) + "]";
	}
	
	
	

}

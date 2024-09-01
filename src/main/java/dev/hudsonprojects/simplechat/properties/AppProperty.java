package dev.hudsonprojects.simplechat.properties;

public enum AppProperty {
	
	PROFILE("spring.profiles.active"),
	JWT_SECRET("jwt.secret", true);
	
	public final String key;
	public final Object defaultValue;
	public final boolean required;
	
	private AppProperty(String key, Object defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.required = false;
	}
	
	private AppProperty(String key, boolean required) {
		this.key = key;
		this.defaultValue = null;
		this.required = required;
	}
	
	private AppProperty(String key) {
		this.key = key;
		this.defaultValue = null;
		this.required = false;
	}
}

package dev.hudsonprojects.simplechat.security;

public abstract class SecurityParams {

	public static final String LOGIN_URI = "/user/login";
	
	public static final long JWT_EXPIRATION_TIME_MILLIS = 60L * 60L * 1000L;
	
	public static final String HEADER_STRING = "Authorization";
    
	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String PRINCIPAL_ATTRIBUTE = "AUTHENTICATED_PRINCIPAL";
	
	public static final String SIGNAL_SEPARATOR = "::";
	
	public static final String INTERNAL_USERNAME_REFRESH_TOKEN_SIGNAL = "refresh_token"+SIGNAL_SEPARATOR;
	
	private SecurityParams() {}
}

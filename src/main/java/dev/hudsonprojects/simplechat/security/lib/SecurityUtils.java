package dev.hudsonprojects.simplechat.security.lib;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface SecurityUtils {
	
	static String[] decodeBasicAuth(String authorization) {
		if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
		    // Authorization: Basic base64credentials
		    String base64Credentials = authorization.substring("Basic".length()).trim();
		    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		    // credentials = username:password
		    return credentials.split(":", 2);
		}
		return new String[] {null, null};
	}
}

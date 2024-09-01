package dev.hudsonprojects.simplechat.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import dev.hudsonprojects.simplechat.common.lib.io.IOUtils;
import dev.hudsonprojects.simplechat.security.lib.SecurityUtils;
import dev.hudsonprojects.simplechat.security.refreshtoken.RefreshToken;
import dev.hudsonprojects.simplechat.security.refreshtoken.RefreshTokenService;
import dev.hudsonprojects.simplechat.user.User;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private AuthenticationManager authenticationManager;
	
	private String jwtSecret;
	private RefreshTokenService refreshTokenService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, String jwtSecret) {
		this.authenticationManager = authenticationManager;
		this.refreshTokenService = refreshTokenService;
		this.jwtSecret = jwtSecret;

		setFilterProcessesUrl(SecurityParams.LOGIN_URI);
	}

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
    	

    	res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Headers", "*");
		
		if(req.getMethod().equals("OPTIONS")) {
			res.setStatus(200);
			return null;
		}
    	
        try(InputStream in = req.getInputStream()) {
        	
        	String authorization = req.getHeader("Authorization");
        	String[] creds = new String[2];
        	if(authorization != null && !authorization.isBlank()) {
        		creds = SecurityUtils.decodeBasicAuth(authorization);
        	} else {
        		String rawBody = IOUtils.readAllText(in);
        		Map<String,String> body = readAuthenticationBody(rawBody);
        		if(!body.isEmpty() && ("refresh_token".equals(body.get("grant_type")))) {
        			creds[0] = SecurityParams.INTERNAL_USERNAME_REFRESH_TOKEN_SIGNAL + body.get("refresh_token");
        			creds[1] = creds[0];
        		} else if(!body.isEmpty() && ("client_credentials".equals(body.get("grant_type")))) {
        			creds[0] = body.get("client_id");
        			creds[1] = body.get("client_secret");
        		}
        	}
        	

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		creds[0],
                    		creds[1],
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Map<String, String> readAuthenticationBody(String rawBody) {
    	try {
    		ObjectMapper mapper;
    		TypeFactory factory;
    		MapType type;
    		factory = TypeFactory.defaultInstance();
    		type    = factory.constructMapType(HashMap.class, String.class, String.class);
    		mapper  = new ObjectMapper();
    		return mapper.readValue(rawBody, type);
    	} catch(Exception e) {
	    	if(rawBody == null || rawBody.isBlank()) {
	    		return new HashMap<>();
	    	}
	    	Map<String, String> body = new HashMap<>();
	    	Stream.of(rawBody.split("&"))
	    		.filter(s -> s.matches(".+=.+"))
	    		.map(s -> s.split("="))
	    		.forEach(s -> body.put(s[0], s[1]));
	    	return body;
    	}
    	
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
    	
    	
        User user = (User) auth.getPrincipal();
        res.addHeader("Cache-Control", "no-store");
        res.addHeader("Content-Type", "application/json");
        
        
        if(user.getRefreshToken() != null && !user.getRefreshToken().isBlank()) {
        	res.getWriter().write(OBJECT_MAPPER.writeValueAsString(this.refreshTokenService.generateNewJWT(user.getRefreshToken())));
        	res.getWriter().flush();
        	return;
        }
        
        String token = JWTGenerator.generateJWT(new MyUserInfoDTO(user), this.jwtSecret, SecurityParams.JWT_EXPIRATION_TIME_MILLIS);
        
        
        RefreshToken refreshToken = this.refreshTokenService.createForUser(user.getUserId());
        
        
        SuccessfulAuthenticationResponse body = new SuccessfulAuthenticationResponse.Builder()
        		.setExpiresIn(SecurityParams.JWT_EXPIRATION_TIME_MILLIS/1000L)
        		.setJwtToken(token)
        		.setRefreshToken(refreshToken.getToken())
            	.setTokenType(SecurityParams.TOKEN_PREFIX.trim())
            	.build();
        String response =OBJECT_MAPPER.writeValueAsString(body);
        System.out.println("response " + response);
        res.setContentLength(response.length());
        res.setStatus(HttpServletResponse.SC_OK);
        res.getOutputStream().print(response);
        res.flushBuffer();
    }

	

}


package dev.hudsonprojects.simplechat.security;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.hudsonprojects.simplechat.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private String jwtSecret;
    
	public JWTAuthorizationFilter(AuthenticationManager authManager, String jwtSecret) {
        super(authManager);
		this.jwtSecret = jwtSecret;
    }
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		System.out.println(req.getMethod());
    
		String header = req.getHeader(SecurityParams.HEADER_STRING);
		
		if (header == null || !header.startsWith(SecurityParams.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch(TokenExpiredException e) {
			res.setStatus(401);
			res.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\", error_description=\"The access token expired\"");
			res.setContentType("application/json");
			try(Writer writer = res.getWriter()){
				Map<Object, Object> responseBody = new LinkedHashMap<>();
				responseBody.put("error", "invalid_token");
				responseBody.put("error_description", "The access token expired");
				String json = new ObjectMapper().writeValueAsString(responseBody);
				writer.append(json);
				res.setStatus(401);
			}catch(Exception e1) {
				
			}
			
		} catch(JWTDecodeException e) {
			res.setStatus(401);
			res.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\", error_description=\"The access token is invalid\"");
			res.setContentType("application/json");
			try(Writer writer = res.getWriter()){
				Map<Object, Object> responseBody = new LinkedHashMap<>();
				responseBody.put("error", "invalid_token");
				responseBody.put("error_description", "The access token is invalid");
				String json = new ObjectMapper().writeValueAsString(responseBody);
				writer.append(json);
				res.setStatus(401);
			}catch(Exception e1) {
				
			}
			
		}


	}

	// Reads the JWT from the Authorization header, and then uses JWT to validate
	// the token
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityParams.HEADER_STRING);
		
		if (token != null) {
			// parse the token.
			DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes())).build().verify(token.replace(SecurityParams.TOKEN_PREFIX, ""));
			
			Object principal = null;
			
			if(JWTAuthenticationSubject.USER.toString().equals(decodedJWT.getSubject())) {
				Map<String, Claim> claims = decodedJWT.getClaims();
				String username = claims.get("username").asString();
				Long id = claims.get("userId").asLong();
				
				if(username != null && id != null) {
					User user = new User();
					user.setUsername(username);
					user.setUserId(id);
					principal = user;
				}
			}

			if (principal != null) {
				return new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
			}

			return null;
		}

		return null;
	}
}

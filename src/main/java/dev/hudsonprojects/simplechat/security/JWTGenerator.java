package dev.hudsonprojects.simplechat.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;

public abstract class JWTGenerator {

	public static String generateJWT(MyUserInfoDTO user, String jwtSecret, long expirationTimeMillis) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("userId", user.getUserId());
		payload.put("username", user.getUsername());

		return JWT.create().withSubject(JWTAuthenticationSubject.USER.toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMillis)).withPayload(payload)
				.sign(Algorithm.HMAC512(jwtSecret.getBytes()));

	}
}

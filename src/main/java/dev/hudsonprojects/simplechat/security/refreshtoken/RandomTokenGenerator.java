package dev.hudsonprojects.simplechat.security.refreshtoken;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public abstract class RandomTokenGenerator {

	private static final Random RANDOM =  new SecureRandom();
	
	private static final List<String> TOKEN_CHARS = List.of("ABCDEFGHIJKLMNOQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".split(""));
	
	public static String generateRandomToken(int size) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < size; i++) {
			String character = TOKEN_CHARS.get(RANDOM.nextInt(TOKEN_CHARS.size()));
			builder.append(character);
		}
		return builder.toString();
	}
}

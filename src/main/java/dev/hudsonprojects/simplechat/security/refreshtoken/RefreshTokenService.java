package dev.hudsonprojects.simplechat.security.refreshtoken;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.hudsonprojects.simplechat.common.exception.UnauthorizedException;
import dev.hudsonprojects.simplechat.common.messages.builder.ErrorDetailsBuilder;
import dev.hudsonprojects.simplechat.security.JWTGenerator;
import dev.hudsonprojects.simplechat.security.SecurityParams;
import dev.hudsonprojects.simplechat.security.SuccessfulAuthenticationResponse;
import dev.hudsonprojects.simplechat.user.User;
import dev.hudsonprojects.simplechat.user.UserService;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;

@Service
public class RefreshTokenService {

	private static final int TOKEN_SIZE = 64;
	@Autowired
	private RefreshTokenRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Value("${jwt.secret}")
	private String jwtSecret;

	public RefreshToken createForUser(Long userId) {
		
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(RandomTokenGenerator.generateRandomToken(TOKEN_SIZE));
		refreshToken.setUser(new User(userId));
		refreshToken.setActive(true);
		refreshToken.setCreatedAt(OffsetDateTime.now());
		
		this.repository.save(refreshToken);
		
		return this.repository.findById(refreshToken.getToken()).orElse(refreshToken);
	}

	public SuccessfulAuthenticationResponse generateNewJWT(String refreshToken) {
		
		RefreshToken refreshTokenDetails = this.repository.findById(refreshToken)
				.orElseThrow(() -> new UnauthorizedException(ErrorDetailsBuilder.unauthorized().setMessage("refreshToken.notFound").build()));
		
		MyUserInfoDTO user = userService.findById(refreshTokenDetails.getUser().getUserId());
		
		String jwt = JWTGenerator.generateJWT(user, jwtSecret, SecurityParams.JWT_EXPIRATION_TIME_MILLIS);
		
		return new SuccessfulAuthenticationResponse.Builder()
        		.setExpiresIn(SecurityParams.JWT_EXPIRATION_TIME_MILLIS/1000)
        		.setJwtToken(jwt)
        		.setRefreshToken(null)
            	.setTokenType(SecurityParams.TOKEN_PREFIX.trim())
            	.build();
		
	}

	public void revoke(String token) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(token);
		this.repository.delete(refreshToken);
	}

	public Optional<RefreshToken> find(String token) {
		return this.repository.findById(token);
	}

}

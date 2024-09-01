package dev.hudsonprojects.simplechat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.hudsonprojects.simplechat.common.exception.NotFoundException;
import dev.hudsonprojects.simplechat.common.exception.ValidationException;
import dev.hudsonprojects.simplechat.common.messages.builder.ErrorDetailsBuilder;
import dev.hudsonprojects.simplechat.common.validation.ValidationService;
import dev.hudsonprojects.simplechat.common.validation.groups.OnUpdate;
import dev.hudsonprojects.simplechat.security.SecurityParams;
import dev.hudsonprojects.simplechat.security.annotation.SecureUserOwned;
import dev.hudsonprojects.simplechat.security.refreshtoken.RefreshToken;
import dev.hudsonprojects.simplechat.security.refreshtoken.RefreshTokenRepository;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;
import dev.hudsonprojects.simplechat.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private ValidationService validationService;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	
	public MyUserInfoDTO findById(Long id) {
		return this.userRepository.findUserInfoById(id).orElseThrow(() -> new NotFoundException(ErrorDetailsBuilder.buildWithMessage("user.validation.id.notFound", id)));
	}
	
	public MyUserInfoDTO findByUsername(String username) {
		return this.userRepository.findUserInfoByUsername(username).orElseThrow(() -> new NotFoundException(ErrorDetailsBuilder.buildWithMessage("user.validation.username.notFound", username)));
	}
	

	@SecureUserOwned
	public MyUserInfoDTO update(User user) {
		validateUpdate(user);
		if(userRepository.existsByEmailAndUserIdNot(user.getEmail(), user.getUserId())) {
			throw new ValidationException(ErrorDetailsBuilder.buildWithFieldError("email", "user.validation.email.exists", user.getEmail()));
		}
		
		this.userRepository.update(user);
		return findById(user.getUserId());
	}

	private void validateUpdate(User user) {
		this.validationService.applyValidation(user, OnUpdate.class);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username " + username);
		if(username != null && username.startsWith(SecurityParams.INTERNAL_USERNAME_REFRESH_TOKEN_SIGNAL)) {
			System.out.println("username está com signal " + username);
			String refreshToken = username.split(SecurityParams.SIGNAL_SEPARATOR)[1];
			return refreshTokenRepository.findById(refreshToken)
					.filter(RefreshToken::isActive)
					.map(RefreshToken::getUser)
					.map(user -> {
						user.setRefreshToken(refreshToken);
						return user;
					})
					.orElseThrow(() -> new UsernameNotFoundException(username));
		}
		return this.userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
}

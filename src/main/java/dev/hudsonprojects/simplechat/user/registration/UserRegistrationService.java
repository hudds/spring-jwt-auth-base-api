package dev.hudsonprojects.simplechat.user.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.hudsonprojects.simplechat.common.exception.ValidationException;
import dev.hudsonprojects.simplechat.common.messages.builder.ErrorDetailsBuilder;
import dev.hudsonprojects.simplechat.user.User;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;
import dev.hudsonprojects.simplechat.user.registration.dto.UserRegistrationDTO;
import dev.hudsonprojects.simplechat.user.repository.UserRepository;

@Service
public class UserRegistrationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	public MyUserInfoDTO save(UserRegistrationDTO registrationData) {
		if(userRepository.existsByEmail(registrationData.getEmail())) {
			throw new ValidationException(ErrorDetailsBuilder.buildWithFieldError("email", "user.validation.email.exists", registrationData.getEmail()));
		}
		
		if(userRepository.existsByUsername(registrationData.getUsername())) {
			throw new ValidationException(ErrorDetailsBuilder.buildWithFieldError("username", "user.validation.username.exists", registrationData.getUsername()));
		}
		registrationData.setPassword(passwordEncoder.encode(registrationData.getPassword()));
	
		User user = registrationData.toUser();
		this.userRepository.save(user);
		return new MyUserInfoDTO(user);
	}
	
	
}

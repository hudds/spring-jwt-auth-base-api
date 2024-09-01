package dev.hudsonprojects.simplechat.user.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;
import dev.hudsonprojects.simplechat.user.registration.dto.UserRegistrationDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/registration")
public class RegistrationController {

	private UserRegistrationService service;
	
	@Autowired
	public RegistrationController(UserRegistrationService service) {
		this.service = service;
	}
	
	@PostMapping
	public MyUserInfoDTO save(@Valid @RequestBody UserRegistrationDTO user) {
		return service.save(user);
	}
	
}

package dev.hudsonprojects.simplechat.user.registration.dto;

import java.io.Serializable;

import dev.hudsonprojects.simplechat.common.validation.groups.OnCreate;
import dev.hudsonprojects.simplechat.common.validation.groups.OnUpdate;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByUsernameProperty;
import dev.hudsonprojects.simplechat.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Size(min = 3, max=512)
	@NotBlank(groups = {OnCreate.class, OnUpdate.class})
	private String name;
	
	@Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+$", message = "user.validation.username.invalid")
	@Size(min = 3,max=512, groups = {OnCreate.class})
	@NotBlank(groups = {OnCreate.class})
	@UserBoundByUsernameProperty
	private String username;
	
	@Email
	@Size(min = 3,max=512)
	@NotBlank()
	private String email;
	
	@Size(min=8, max=256, message = "validation.user.registration.password.size")
	@Pattern(regexp = ".*[a-z].*", message = "validation.user.registration.password.mustContainLowerCaseLetter")
	@Pattern(regexp = ".*[A-Z].*", message = "validation.user.registration.validation.password.mustContainUpperCaseLetter")
	@Pattern(regexp = ".*\\d.*", message = "validation.user.registration.password.mustContainDigit")
	@NotBlank(message = "validation.user.registration.password.notBlank")
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public User toUser() {
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		user.setUsername(username);
		return user;
	}

}

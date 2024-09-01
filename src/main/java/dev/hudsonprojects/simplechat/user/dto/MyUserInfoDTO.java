package dev.hudsonprojects.simplechat.user.dto;

import java.io.Serializable;

import dev.hudsonprojects.simplechat.user.User;

public class MyUserInfoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String name;
	private String username;
	private String email;

	public MyUserInfoDTO() {}
	
	public MyUserInfoDTO(User user) {
		this.userId = user.getUserId();
		this.name = user.getName();
		this.username = user.getUsername();
		this.email = user.getEmail();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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
	
	

}

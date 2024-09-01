package dev.hudsonprojects.simplechat.user;

import java.util.Collection;

import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dev.hudsonprojects.simplechat.common.validation.groups.OnCreate;
import dev.hudsonprojects.simplechat.common.validation.groups.OnUpdate;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByIdProperty;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByUsernameProperty;
import dev.hudsonprojects.simplechat.security.annotation.UserOwned;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "\"user\"", schema="\"user\"")
@UserOwned
public class User implements UserDetails{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = OnUpdate.class)
	@UserBoundByIdProperty
	@Column(name = "user_id")
	private Long userId;
	
	@Size(min = 3,max=512)
	@NotBlank(groups = {OnCreate.class, OnUpdate.class})
	private String name;
	
	@Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+$", message = "user.validation.username.invalid")
	@Size(min = 3,max=512, groups = {OnCreate.class})
	@NotBlank(groups = {OnCreate.class})
	@Column(unique = true)
	@UserBoundByUsernameProperty
	private String username;
	
	@Email
	@Size(min = 3,max=512)
	@NotBlank(groups = {OnCreate.class, OnUpdate.class})
	@Column(unique = true)
	private String email;
	
	@Size(min=8, max=256)
	@Pattern(regexp = ".*[a-z].*", message = "user.validation.password.mustContainLowerCaseLetter")
	@Pattern(regexp = ".*[A-Z].*", message = "user.validation.password.mustContainUpperCaseLetter")
	@Pattern(regexp = ".*\\d.*", message = "user.validation.password.mustContainDigit")
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank(groups = {OnCreate.class})
	private String password;
	
	@Transient
	@JsonIgnore
	private String refreshToken;
	
	public User() {}
	
	public User(Long userId) {
		this.userId = userId;
		
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
	@Override
	public String toString() {
		return "User [id=" + userId + ", name=" + name + ", username=" + username + ", email=" + email + "]";
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}

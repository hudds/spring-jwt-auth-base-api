package dev.hudsonprojects.simplechat.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import dev.hudsonprojects.simplechat.properties.AppProperty;
import dev.hudsonprojects.simplechat.properties.PropertiesService;
import dev.hudsonprojects.simplechat.security.JWTAuthenticationFilter;
import dev.hudsonprojects.simplechat.security.JWTAuthorizationFilter;
import dev.hudsonprojects.simplechat.security.SecurityParams;
import dev.hudsonprojects.simplechat.security.refreshtoken.RefreshTokenService;
import dev.hudsonprojects.simplechat.user.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private PropertiesService propertiesService;

	@Bean
	public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider impl = new DaoAuthenticationProvider();
		impl.setUserDetailsService(userDetailsService());
		impl.setHideUserNotFoundExceptions(false);
		impl.setPasswordEncoder(passwordEncoder);
		return impl;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration,
			RefreshTokenService refreshTokenService) throws Exception {

		http.authorizeHttpRequests(authz -> authz.requestMatchers(HttpMethod.POST, "/user").permitAll()
				.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
				.requestMatchers(SecurityParams.LOGIN_URI).permitAll()
				.requestMatchers(HttpMethod.GET, "refresh-token").permitAll()
				.requestMatchers(HttpMethod.GET, "/metatata/*").permitAll()
				.requestMatchers(HttpMethod.DELETE, "refresh-token").permitAll()
				.anyRequest().permitAll())
				
//				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(sessionManagemenent -> sessionManagemenent
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(),
						refreshTokenService, propertiesService.getProperty(AppProperty.JWT_SECRET, String.class)))
				.addFilter(new JWTAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(),
						propertiesService.getProperty(AppProperty.JWT_SECRET, String.class)))
				.userDetailsService(userService);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "TRACE", "PATCH"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return this.userService;
	}
}

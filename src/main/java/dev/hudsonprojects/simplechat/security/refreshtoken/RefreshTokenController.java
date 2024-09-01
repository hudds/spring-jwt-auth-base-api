package dev.hudsonprojects.simplechat.security.refreshtoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.hudsonprojects.simplechat.security.SuccessfulAuthenticationResponse;

@RestController
@RequestMapping("refresh-token")
public class RefreshTokenController {
	
	@Autowired
	private RefreshTokenService service;

	@GetMapping
	public SuccessfulAuthenticationResponse renewToken(@RequestHeader("refresh-token") String refreshToken){
		return service.generateNewJWT(refreshToken);
	}
	
	@DeleteMapping
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void revokeToken(@RequestHeader("refresh-token") String refreshToken){
		service.revoke(refreshToken);
	}
	
}

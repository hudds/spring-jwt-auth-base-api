package dev.hudsonprojects.simplechat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserSecureService service;
	
	@PutMapping
	public MyUserInfoDTO update(@RequestBody User user) {
		return service.update(user);
	}
	
	@GetMapping("info")
	public MyUserInfoDTO getInfo() {
		return service.findByUsername();
	}
	
}

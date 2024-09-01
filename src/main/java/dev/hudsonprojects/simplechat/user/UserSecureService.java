package dev.hudsonprojects.simplechat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hudsonprojects.simplechat.requestdata.RequestData;
import dev.hudsonprojects.simplechat.security.annotation.SecureUserOwned;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByIdProperty;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByUsernameProperty;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;

@Service
public class UserSecureService {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RequestData requestData;
		
	@SecureUserOwned
	public MyUserInfoDTO findById(@UserBoundByIdProperty Long id) {
		return userService.findById(id); 
	}
	
	@SecureUserOwned
	public MyUserInfoDTO findByUsername(@UserBoundByUsernameProperty String username) {
		return this.userService.findByUsername(username);
	}
	
	
	public MyUserInfoDTO findById() {
		return userService.findById(requestData.getUserOrUnauthorized().getUserId()); 
	}
	
	
	public MyUserInfoDTO findByUsername() {
		return this.userService.findByUsername(requestData.getUserOrUnauthorized().getUsername());
	}
	
	@SecureUserOwned
	public MyUserInfoDTO update(User user) {
		return this.userService.update(user);
	}

}

package dev.hudsonprojects.simplechat.user.repository;

import java.util.Optional;

import dev.hudsonprojects.simplechat.user.User;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;

public interface UserRepositoryCustom {
	
	void update(User user);

	void updatePassword(User user);
	
	Optional<MyUserInfoDTO> findUserInfoById(Long userId);
	
	Optional<MyUserInfoDTO> findUserInfoByUsername(String username);
}

package dev.hudsonprojects.simplechat.user.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.hudsonprojects.simplechat.user.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom{

	boolean existsByEmail(String email);

	boolean existsByUsername(String email);

	boolean existsByEmailAndUserIdNot(String email, Long id);

	Optional<User> findByUsernameOrEmail(String username, String email);

	Optional<User> findByUsername(String username);
	
}

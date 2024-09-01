package dev.hudsonprojects.simplechat.user.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.hudsonprojects.simplechat.user.User;
import dev.hudsonprojects.simplechat.user.dto.MyUserInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@Autowired
	private EntityManager entityManager;

	@Override
	public void update(User user) {
		String hql = "update User u set u.email = :email, u.name = :name";
		entityManager.createQuery(hql).setParameter("email", user.getEmail()).setParameter("name", user.getName()).executeUpdate();
	}
	

	@Override
	public void updatePassword(User user) {
		String hql = "update User u set u.password = :password";
		
		entityManager.createQuery(hql).setParameter("password", user.getPassword()).executeUpdate();
		
	}
	
	@Override
	public Optional<MyUserInfoDTO> findUserInfoById(Long userId){
		
		String sql = "SELECT u.user_id, u.name, u.username, u.email FROM \"user\".\"user\" u WHERE u.user_id = :userId";
		
		try {
			
			Tuple result = (Tuple) entityManager.createNativeQuery(sql, Tuple.class)
					.setParameter("userId", userId)
					.getSingleResult();
			
			
			MyUserInfoDTO dto = mapToUserInfo(result);

			return Optional.of(dto);
			
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public Optional<MyUserInfoDTO> findUserInfoByUsername(String username){
		String sql = "SELECT u.user_id, u.name, u.username, u.email FROM \"user\".\"user\" u WHERE u.username = :username";
		try {
			
			Tuple result = (Tuple) entityManager.createNativeQuery(sql, Tuple.class)
					.setParameter("username", username)
					.getSingleResult();
			
			MyUserInfoDTO dto = mapToUserInfo(result);
			return Optional.of(dto);
			
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}


	private MyUserInfoDTO mapToUserInfo(Tuple result) {
		MyUserInfoDTO dto = new MyUserInfoDTO();
		dto.setUserId(result.get("user_id", Long.class));
		dto.setEmail(result.get("email", String.class));
		dto.setName(result.get("name", String.class));
		dto.setUsername(result.get("username", String.class));
		return dto;
	}
	
}

package com.bioinnovate.PreMediT.backend.services;

import java.util.List;
import java.util.Optional;

import com.bioinnovate.PreMediT.backend.entities.UserRole;
import com.bioinnovate.PreMediT.backend.entities.User;
import com.bioinnovate.PreMediT.backend.repositories.UserRepository;
import com.bioinnovate.PreMediT.backend.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.artur.helpers.CrudService;



@Service
public class UserService extends CrudService<User, Integer> {

	private static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";
	private static final String USER_NOT_FOUND = "User not found";
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private UserRoleRepository userRoleRepository;

	@Autowired
	public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,UserRoleRepository userRoleRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}


    public List<UserRole> findAllRoles(){
		return userRoleRepository.findAll();
	}

	@Override
	protected UserRepository getRepository() {
		return userRepository;
	}



	public String encodePassword(String value) {
		return passwordEncoder.encode(value);
	}


	@Transactional
	public User save(User entity) {
		throwIfUserLocked(entity.getId());
		return super.update(entity);
	}

	@Override
	@Transactional
	public void delete(Integer userId) {
		throwIfUserLocked(userId);
		super.delete(userId);
	}

	private void throwIfUserLocked(Integer userId) {
		if (userId == null) {
			return;
		}

		Optional<User> dbUser = getRepository().findById(userId);
		if (!dbUser.isPresent()) {
		    throw new UserFriendlyDataException(USER_NOT_FOUND);
		}
	}

	public User findByEmail(String email){
		return userRepository.findByEmail(email);
	}


}

package com.bioinnovate.Template.backend.repositories;

import com.bioinnovate.Template.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

}

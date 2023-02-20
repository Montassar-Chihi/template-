package com.bioinnovate.PreMediT.backend.repositories;

import com.bioinnovate.PreMediT.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

}

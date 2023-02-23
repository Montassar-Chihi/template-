package com.bioinnovate.Template.backend.repositories;

import com.bioinnovate.Template.backend.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query(value = "SELECT * FROM user_role WHERE id!=0"
            ,nativeQuery = true)
    List<UserRole> findAll();
}

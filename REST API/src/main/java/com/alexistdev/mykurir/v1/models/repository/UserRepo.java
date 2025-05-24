package com.alexistdev.mykurir.v1.models.repository;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface  UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role !=:role")
    Page<User> findByRoleNot(@Param("role") Role role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role !=:role AND u.email LIKE %:filter% OR u.fullName LIKE %:filter%")
    Page<User> findByFilter(@Param("role") Role role,@Param("filter")  String filter, Pageable pageable);
}

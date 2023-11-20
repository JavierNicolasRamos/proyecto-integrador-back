package com.proyecto.integrador.repository;

import com.proyecto.integrador.dto.AuthDto;
import com.proyecto.integrador.entity.User;

import com.proyecto.integrador.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = :email AND deleted = false", nativeQuery = true)
    User findByEmail(@Param("email")String email);

    @Query(value = "SELECT * FROM users WHERE deleted = false", nativeQuery = true)
    List<User> findAllByDeletedFalse();

    @Query(value = "SELECT * FROM users WHERE is_admin = true AND deleted = false", nativeQuery = true)
    List<User> findAllAdminUsersByDeletedFalse();

    @Query(value = "SELECT * FROM users WHERE is_admin = false AND deleted = false", nativeQuery = true)
    List<User> findAllNormalUsersByDeletedFalse();

    @Query(value = "SELECT * FROM users WHERE id = :id AND deleted = false", nativeQuery = true)
    User findByIdAndDeletedFalse(@Param("id")Long id);

    @Query(value = "SELECT * FROM users WHERE user_role = :role AND deleted = false", nativeQuery = true)
    User findByRole(@Param("role")String role);

    @Query(value = "SELECT user_role FROM users WHERE email = :email AND deleted = false", nativeQuery = true)
    Role getRoleByEmail(String email);
}

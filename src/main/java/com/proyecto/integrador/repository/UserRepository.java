package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = :email AND deleted = false", nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE deleted = false", nativeQuery = true)
    List<User> findAllByDeletedFalse();

    @Query(value = "SELECT * FROM users WHERE is_admin = true AND deleted = false", nativeQuery = true)
    List<User> findAllAdminUsersByDeletedFalse();

    @Query(value = "SELECT * FROM users WHERE is_admin = false AND deleted = false", nativeQuery = true)
    List<User> findAllNormalUsersByDeletedFalse();

    @Query(value = "SELECT * FROM users WHERE id = :id AND deleted = false", nativeQuery = true)
    User findByIdAndDeletedFalse(Long id);

    @Query(value = "UPDATE users SET deleted = true WHERE id = :id", nativeQuery = true)
    User deleteUsersById(Long id);

    @Query(value = "UPDATE users SET name = :name, surname = :surname, is_admin = :isAdmin, area_code = :areaCode, prefix = :prefix, phone = :phone, is_mobile = :isMobile, email = :email, password = :password WHERE id = :id", nativeQuery = true)
    User updateUserById(Long id, User user);
}

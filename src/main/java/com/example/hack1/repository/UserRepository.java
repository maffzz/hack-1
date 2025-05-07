package com.example.hack1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hack1.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Para autenticación y gestión de usuarios
    List<User> findByCompanyId(Long companyId);
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
}
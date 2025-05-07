package com.example.hack1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hack1.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Para autenticación y gestión de usuarios
    Optional<User> findByEmail(String email);
    List<User> findByCompanyId(Long companyId);
    boolean existsByEmail(String email);
}
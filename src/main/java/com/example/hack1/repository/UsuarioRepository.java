package com.example.hack1.repository;

import com.example.hack1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<User, Long> {
    User findByNombreDeUsuario(String nombreDeUsuario);}
package com.example.hack1.repository;

import com.example.hack1.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Para activar/desactivar empresas
    boolean existsByRuc(String ruc);
    // Para obtener empresas con estado específico
    List<Company> findByActive(boolean active);
}
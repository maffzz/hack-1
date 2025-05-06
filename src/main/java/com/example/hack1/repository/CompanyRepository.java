package com.example.hack1.repository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Para activar/desactivar empresas
    boolean existsByRuc(String ruc);

    // Para obtener empresas con estado específico
    List<Company> findByActive(boolean active);
}
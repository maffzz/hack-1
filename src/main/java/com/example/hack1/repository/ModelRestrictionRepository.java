package com.example.hack1.repository;

public interface ModelRestrictionRepository extends JpaRepository<ModelRestriction, Long> {
    // Para gestión de restricciones
    List<ModelRestriction> findByCompanyId(Long companyId);
    boolean existsByCompanyAndModelProviderAndModelName(
            Company company, String provider, String modelName);
}
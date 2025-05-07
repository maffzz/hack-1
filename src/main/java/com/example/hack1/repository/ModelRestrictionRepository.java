package com.example.hack1.repository;

import com.example.hack1.domain.Company;
import com.example.hack1.domain.ModelRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModelRestrictionRepository extends JpaRepository<ModelRestriction, Long> {
    // Para gestión de restricciones
    List<ModelRestriction> findByCompanyId(Long companyId);
    boolean existsByCompanyAndModelProviderAndModelName(
            Company company, String provider, String modelName);
    Optional<Object> findByCompanyAndModelProviderAndModelName(Company company, String modelProvider, String modelName);
    void deleteByIdAndCompanyId(Long id, Long companyId);
}
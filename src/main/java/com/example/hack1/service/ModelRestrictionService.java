package com.example.hack1.service;

import com.example.hack1.domain.Company;
import com.example.hack1.domain.ModelRestriction;
import com.example.hack1.repository.ModelRestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModelRestrictionService {
    @Autowired
    private ModelRestrictionRepository restrictionRepo;

    public ModelRestriction createRestriction(Long companyId, ModelRestriction restriction) {
        restriction.setCompany(new Company(companyId)); // Evita fetch innecesario
        return restrictionRepo.save(restriction);
    }

    public void validateCompanyLimits(Company company, String provider, String model) {
        restrictionRepo.findByCompanyAndModelProviderAndModelName(company, provider, model)
                .ifPresent(restriction -> {
                    // Lógica de validación contra límites empresariales
                });
    }
}

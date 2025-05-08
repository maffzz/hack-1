package com.example.hack1.controller;

import com.example.hack1.domain.Company;
import com.example.hack1.domain.ModelRestriction;
import com.example.hack1.repository.ModelRestrictionRepository;
import com.example.hack1.service.ModelRestrictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/restrictions")
@RequiredArgsConstructor
public class CompanyRestrictionController {

    private final ModelRestrictionService restrictionService;
    private final ModelRestrictionRepository restrictionRepo;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ModelRestriction createRestriction(
            @RequestHeader("X-Company-ID") Long companyId,
            @RequestBody ModelRestriction restriction) {
        return restrictionService.createRestriction(companyId, restriction);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public List<ModelRestriction> getRestrictions(@RequestHeader("X-Company-ID") Long companyId) {
        return restrictionRepo.findByCompanyId(companyId); // Asumiendo que restrictionRepo está accesible
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ModelRestriction updateRestriction(
            @PathVariable Long id,
            @RequestHeader("X-Company-ID") Long companyId,
            @RequestBody ModelRestriction restriction) {
        restriction.setId(id);
        Company company = new Company();
        company.setId(companyId);
        restriction.setCompany(company);
        return restrictionRepo.save(restriction);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public void deleteRestriction(
            @PathVariable Long id,
            @RequestHeader("X-Company-ID") Long companyId) {
        restrictionRepo.deleteByIdAndCompanyId(id, companyId);
    }
}


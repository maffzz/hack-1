package com.example.hack1.controller;

import com.example.hack1.domain.AIRequest;
import com.example.hack1.domain.Company;
import com.example.hack1.domain.User;
import com.example.hack1.dto.CreateCompanyDTO;
import com.example.hack1.exception.NotFound;
import com.example.hack1.repository.AIRequestRepository;
import com.example.hack1.repository.CompanyRepository;
import com.example.hack1.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRepository companyRepo;
    private final AIRequestRepository aiRequestRepo;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public ResponseEntity<Company> createCompany(@RequestBody CreateCompanyDTO dto) {
        User adminUser = new User();
        adminUser.setEmail(dto.getAdmin().getEmail());
        adminUser.setPassword(dto.getAdmin().getPassword()); // deberías codificarla si usas Spring Security

        Company company = new Company();
        company.setRuc(dto.getRuc());
        company.setName(dto.getName());
        company.setActive(dto.isActive());

        Company createdCompany = companyService.createCompany(company, adminUser);
        return ResponseEntity.created(URI.create("/companies/" + createdCompany.getId())).body(createdCompany);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public List<Company> getAllCompanies() {
        return companyService.findAll(); // Asumiendo que companyRepo está accesible
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public Company getCompany(@PathVariable Long id) {
        return companyRepo.findById(id).orElseThrow(() -> new NotFound("Empresa no encontrada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company company) {
        company.setId(id);
        return companyRepo.save(company);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public void toggleCompanyStatus(@PathVariable Long id, @RequestParam boolean active) {
        companyService.toggleCompanyStatus(id, active);
    }

    @GetMapping("/{id}/consumption")
    @PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
    public Map<String, Object> getCompanyConsumption(@PathVariable Long id) {
        List<AIRequest> requests = aiRequestRepo.findByCompanyId(id);
        Map<String, Integer> consumption = new HashMap<>();
        requests.forEach(req -> {
            String key = req.getModelProvider() + "|" + req.getModelName();
            consumption.merge(key, req.getTokensConsumed(), Integer::sum);
        });
        return Map.of(
                "companyId", id,
                "consumptionByModel", consumption,
                "totalRequests", requests.size()
        );
    }
}


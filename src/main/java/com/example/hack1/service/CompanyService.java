package com.example.hack1.service;

import com.example.hack1.domain.Company;
import com.example.hack1.domain.Role;
import com.example.hack1.domain.User;
import com.example.hack1.repository.CompanyRepository;
import com.example.hack1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired private UserRepository userRepo;

    public List<Company> findAll() {
        return companyRepo.findAll();}

    public Company createCompany(Company company, User adminUser) {
        adminUser.setRole(Role.ROLE_COMPANY_ADMIN);
        userRepo.save(adminUser);

        company.setAdmin(adminUser);
        company.setAffiliationDate(LocalDate.now());
        return companyRepo.save(company);
    }

    public void toggleCompanyStatus(Long companyId, boolean active) {
        companyRepo.findById(companyId)
                .ifPresent(c -> c.setActive(active));
    }
}

package com.example.hack1.service;

import com.example.hack1.domain.Company;
import com.example.hack1.domain.Role;
import com.example.hack1.domain.User;
import com.example.hack1.domain.UserLimit;
import com.example.hack1.exception.NotFound;
import com.example.hack1.repository.CompanyRepository;
import com.example.hack1.repository.UserLimitRepository;
import com.example.hack1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired private UserLimitRepository limitRepo;
    @Autowired private CompanyRepository companyRepo;

    public User createUser(User user, Long companyId) {
        Company com = companyRepo.findById(companyId).orElseThrow(
                () -> new NotFound("Empresa no encontrada"));
        user.setCompany(com);
        user.setRole(Role.ROLE_USER);
        return userRepo.save(user);}

    public void assignUserLimit(Long userId, UserLimit limit) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFound("Usuario no encontrado"));

        limit.setUser(user);
        limit.setLastReset(LocalDateTime.now());
        limitRepo.save(limit);
    }
}

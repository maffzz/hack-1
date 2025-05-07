package com.example.hack1.controller;

import com.example.hack1.domain.AIRequest;
import com.example.hack1.domain.User;
import com.example.hack1.domain.UserLimit;
import com.example.hack1.exception.NotFound;
import com.example.hack1.repository.AIRequestRepository;
import com.example.hack1.repository.UserRepository;
import com.example.hack1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// UserManagementController.java
@RestController
@RequestMapping("/api/company/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final AIRequestRepository aiRequestRepo;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public User createUser(
            @RequestHeader("X-Company-ID") Long companyId,
            @RequestBody User user) {
        return userService.createUser(user, companyId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public List<User> getCompanyUsers(@RequestHeader("X-Company-ID") Long companyId) {
        return userRepo.findByCompanyId(companyId); // Asumiendo que userRepo está accesible
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public User getUser(@PathVariable Long id, @RequestHeader("X-Company-ID") Long companyId) {
        return userRepo.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFound("Usuario no encontrado"));}

    // Endpoint PUT básico (solo actualiza campos necesarios)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    @Transactional
    public User updateUserEmail( // Renombrado para claridad
                                 @PathVariable Long id,
                                 @RequestHeader("X-Company-ID") Long companyId,
                                 @RequestBody Map<String, String> request) { // Solo acepta email
        User user = userRepo.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFound("Usuario no encontrado"));

        // Actualizar solo email si viene en el request
        if (request.containsKey("email")) {
            user.setEmail(request.get("email"));
        }

        return userRepo.save(user);
    }


    @PostMapping("/{id}/limits")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public UserLimit assignUserLimit(
            @PathVariable Long id,
            @RequestHeader("X-Company-ID") Long companyId,
            @RequestBody UserLimit limit) {
        userService.assignUserLimit(id, limit);
        return limit;
    }

    @GetMapping("/{id}/consumption")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN', 'ROLE_USER')")
    public Map<String, Object> getUserConsumption(
            @PathVariable Long id,
            @RequestHeader("X-Company-ID") Long companyId) {
        List<AIRequest> requests = aiRequestRepo.findByUserIdAndCompanyId(id, companyId);
        return Map.of(
                "userId", id,
                "totalTokensUsed", requests.stream().mapToInt(AIRequest::getTokensConsumed).sum(),
                "requestsCount", requests.size()
        );
    }
}
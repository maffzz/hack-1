package com.example.hack1.service;

import com.example.hack1.domain.*;
import com.example.hack1.exception.Conflict;
import com.example.hack1.exception.NotFound;
import com.example.hack1.repository.CompanyRepository;
import com.example.hack1.repository.ModelRestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModelRestrictionService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ModelRestrictionRepository modelRestrictionRepository;

    public void validateModelAccess(Company company, String modelProvider) {
        // Verificar si la compañía tiene acceso al proveedor de modelos
        boolean hasAccess = company.getRestrictions().stream()
                .filter(r -> r.getModelProvider().equalsIgnoreCase(modelProvider))
                .anyMatch(ModelRestriction::isAllowed);

        if (!hasAccess) {
            throw new Conflict("Company does not have access to this model provider");
        }
    }

    @Transactional
    public ModelRestriction createRestriction(Long companyId, ModelRestriction restriction) {
        // 1. Validar que la compañía existe
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFound("Company not found with id: " + companyId));

        // 2. Validar que el modelProvider es válido
        validateModelProvider(restriction.getModelProvider());

        // 3. Verificar si ya existe una restricción para este proveedor en la compañía
        Optional<ModelRestriction> existingRestriction = company.getRestrictions().stream()
                .filter(r -> r.getModelProvider().equalsIgnoreCase(restriction.getModelProvider()))
                .findFirst();

        if (existingRestriction.isPresent()) {
            // Actualizar la restricción existente
            ModelRestriction existing = existingRestriction.get();
            existing.setAllowed(restriction.isAllowed());
            existing.setDailyTokenLimit(restriction.getDailyTokenLimit());
            return modelRestrictionRepository.save(existing);
        } else {
            // Crear nueva restricción
            restriction.setCompany(company);
            ModelRestriction savedRestriction = modelRestrictionRepository.save(restriction);
            company.getRestrictions().add(savedRestriction);
            companyRepository.save(company);
            return savedRestriction;
        }
    }

    private void validateModelProvider(String modelProvider) {
        List<String> validProviders = List.of("OPENAI", "META", "DEEPSEEK", "GITHUB");
        if (!validProviders.contains(modelProvider.toUpperCase())) {
            throw new IllegalArgumentException("Invalid model provider. Valid providers are: " + validProviders);
        }
    }

    public void checkUserLimits(User user, String modelProvider, String modelName) {
        // Verificar si el modelo está disponible para el usuario
        if (!getAvailableModelsForUser(user).contains(modelName)) {
            throw new Conflict("User does not have access to this model");
        }

        // Verificar límites de tokens del usuario para el proveedor específico
        UserLimit userLimit = user.getLimits().stream()
                .filter(limit -> limit.getModelProvider().equalsIgnoreCase(modelProvider))
                .findFirst()
                .orElseThrow(() -> new Conflict("No limit configuration found for this model provider"));

        // Resetear el contador si es un nuevo día
        if (userLimit.getLimitDate() == null || !userLimit.getLimitDate().equals(LocalDate.now())) {
            userLimit.setTokensUsed(0);
            userLimit.setLimitDate(LocalDate.now());
        }

        // Verificar si ha excedido el límite
        if (userLimit.getTokenLimit() != null &&
                userLimit.getTokensUsed() >= userLimit.getTokenLimit()) {
            throw new Conflict("User has exceeded daily token quota for this model provider");
        }
    }

    public List<String> getAvailableModelsForUser(User user) {
        // Modelos disponibles basados en el rol del usuario

        // Modelos básicos para todos los usuarios
        Set<String> availableModels = new HashSet<>(List.of(
                "gpt-3.5-turbo",
                "llama-2-7b",
                "deepseek-chat"
        ));

        if (user.getRole() == Role.ROLE_SPARKY_ADMIN || user.getRole() == Role.ROLE_COMPANY_ADMIN || user.getRole() == Role.ROLE_USER) {
            availableModels.addAll(List.of(
                    "gpt-4",
                    "llama-2-13b",
                    "deepseek-coder",
                    "github-model-1"
            ));
        }

        // Filtrar por los proveedores permitidos para la compañía
        return availableModels.stream()
                .filter(model -> isModelAllowedForCompany(user.getCompany(), getProviderFromModel(model)))
                .collect(Collectors.toList());
    }

    public void updateUserUsage(User user, String modelProvider, int tokensUsed) {
        // Encontrar o crear el límite para este usuario y proveedor
        UserLimit userLimit = user.getLimits().stream()
                .filter(limit -> limit.getModelProvider().equalsIgnoreCase(modelProvider))
                .findFirst()
                .orElseGet(() -> {
                    UserLimit newLimit = new UserLimit();
                    newLimit.setUser(user);
                    newLimit.setModelProvider(modelProvider);
                    newLimit.setLimitDate(LocalDate.now());
                    user.getLimits().add(newLimit);
                    return newLimit;
                });

        // Actualizar tokens usados
        userLimit.setTokensUsed(userLimit.getTokensUsed() + tokensUsed);
    }

    private boolean isModelAllowedForCompany(Company company, String provider) {
        return company.getRestrictions().stream()
                .filter(r -> r.getModelProvider().equalsIgnoreCase(provider))
                .anyMatch(ModelRestriction::isAllowed);
    }

    private String getProviderFromModel(String modelName) {
        if (modelName.startsWith("gpt")) return "OPENAI";
        if (modelName.startsWith("llama")) return "META";
        if (modelName.startsWith("deepseek")) return "DEEPSEEK";
        if (modelName.startsWith("github")) return "GITHUB";
        return "UNKNOWN";
    }
}
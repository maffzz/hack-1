//package com.example.hack1.service;
//
//import com.example.hack1.domain.AIResponse;
//import com.example.hack1.domain.User;
//import com.example.hack1.repository.CompanyRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.example.hack1.domain.AIRequest;
//
//@Service
//@Transactional
//@Slf4j
//public class AIService {
//    // Dependencias existentes +
//    @Autowired private CompanyRepository companyRepo;
//    @Autowired private ModelRestrictionService restrictionService;
//
//    public AIResponse processRequest(User user, AIRequest request) {
//        validateRequest(user, request);
//
//        try {
//            AIResponse response = executeModel(request);
//            persistRequest(user, request, response);
//            updateUsageMetrics(user, response);
//            return response;
//
//        } catch (AIServiceException e) {
//            log.error("Error procesando solicitud: {}", e.toString());
//            persistErrorRequest(user, request, e);
//            throw e;
//        }
//    }
//
//    private void validateRequest(User user, AIRequest request) {
//        restrictionService.validateModelAccess(user.getCompany(), request.getModelProvider());
//        checkUserLimits(user, request.getModelProvider(), request.getModelName());
//        validateMultimodalContent(request);
//    }
//
//    private void validateMultimodalContent(AIRequest request) {
//        if (request.isMultimodal() && request.getFiles().size() > 5) {
//            throw new InvalidRequestException("Máximo 5 archivos por solicitud multimodal");
//        }
//    }
//}
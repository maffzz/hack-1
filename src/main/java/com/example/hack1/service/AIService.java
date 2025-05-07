package com.example.hack1.service;

import com.example.hack1.dto.AIResponse;
import com.example.hack1.domain.User;
import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;
import com.example.hack1.exception.BadRequest;
import com.example.hack1.exception.Conflict;
import com.example.hack1.repository.CompanyRepository;
import com.example.hack1.repository.AIRequestRepository;
import com.example.hack1.domain.AIRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class AIService {
    @Autowired private CompanyRepository companyRepo;
    @Autowired private AIRequestRepository aiRequestRepo;
    @Autowired private ModelRestrictionService restrictionService;
    @Autowired private GitHubModelsClient gitHubModelsClient;

    private static final Map<String, String> MODEL_PROVIDERS = Map.of(
            "OPENAI", "OpenAI",
            "META", "Meta",
            "DEEPSEEK", "DeepSeek",
            "GITHUB", "GitHub"
    );

    public AIResponse processRequest(User user, AIRequest request) {
        validateRequest(user, request);

        try {
            AIResponse response = executeModel(request);
            persistRequest(user, request, response);
            updateUsageMetrics(user, response);
            return response;
        } catch (Conflict e) {
            log.error("Error processing request: {}", e.toString());
            persistErrorRequest(user, request, e);
            throw e;
        }
    }

    private void validateRequest(User user, AIRequest request) {
        restrictionService.validateModelAccess(user.getCompany(), request.getModelProvider());
        restrictionService.checkUserLimits(user, request.getModelProvider(), request.getModelName());
        validateMultimodalContent(request);
    }

    private void validateMultimodalContent(AIRequest request) {
        if (request.isMultimodal() && request.getFiles().size() > 5) {
            throw new BadRequest("Maximum 5 files per multimodal request");
        }
    }

    private AIResponse executeModel(AIRequest request) {
        ModelRequest modelRequest = createModelRequest(request);
        ModelResponse modelResponse = switch (request.getModelProvider().toUpperCase()) {
            case "OPENAI" -> gitHubModelsClient.callOpenAIModel(modelRequest);
            case "META" -> gitHubModelsClient.callMetaModel(modelRequest);
            case "DEEPSEEK" -> gitHubModelsClient.callDeepSeekModel(modelRequest);
            case "GITHUB" -> gitHubModelsClient.callGitHubModel(modelRequest);
            default -> throw new BadRequest("Unsupported model provider");
        };

        return convertToAIResponse(modelResponse);
    }

    private ModelRequest createModelRequest(AIRequest request) {
        ModelRequest modelRequest = new ModelRequest();
        modelRequest.setPrompt(request.getQueryText());
        modelRequest.setModelName(request.getModelName());
        modelRequest.setMultimodal(request.isMultimodal());
        if (request.isMultimodal()) {
            modelRequest.setFiles(request.getFiles());
        }
        return modelRequest;
    }

    private AIResponse convertToAIResponse(ModelResponse modelResponse) {
        AIResponse response = new AIResponse();
        response.setText(modelResponse.getGeneratedText());
        response.setTokensUsed(modelResponse.getTokensUsed());
        response.setModelProvider(modelResponse.getModelProvider());
        response.setModelName(modelResponse.getModelName());
        response.setError(modelResponse.isError());
        response.setErrorMessage(modelResponse.getErrorMessage());
        return response;
    }

    private void persistRequest(User user, AIRequest request, AIResponse response) {
        AIRequest entity = new AIRequest();
        entity.setUser(user);
        entity.setCompany(user.getCompany());
        entity.setQueryText(request.getQueryText());
        entity.setResponseText(response.getText());
        entity.setTokensConsumed(response.getTokensUsed());
        entity.setModelProvider(response.getModelProvider());
        entity.setModelName(response.getModelName());
        entity.setTimestamp(LocalDateTime.now());
        aiRequestRepo.save(entity);
    }

    private void persistErrorRequest(User user, AIRequest request, Conflict e) {
        AIRequest entity = new AIRequest();
        entity.setUser(user);
        entity.setCompany(user.getCompany());
        entity.setQueryText(request.getQueryText());
        entity.setTokensConsumed(0);
        entity.setModelProvider(request.getModelProvider());
        entity.setModelName(request.getModelName());
        entity.setTimestamp(LocalDateTime.now());
        aiRequestRepo.save(entity);
    }

    private void updateUsageMetrics(User user, AIResponse response) {

        // Implementación para actualizar métricas de uso
        restrictionService.updateUserUsage(user, response.getModelProvider(), response.getTokensUsed());
    }

    public List<AIRequest> getUserHistory(User user) {
        return aiRequestRepo.findByUserOrderByTimestampDesc(user);
    }

    public List<String> getAvailableModels(User user) {
        return restrictionService.getAvailableModelsForUser(user);
    }
}
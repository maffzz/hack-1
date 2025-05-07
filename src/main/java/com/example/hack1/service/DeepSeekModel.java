package com.example.hack1.service;

import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeepSeekModel implements AIModel {

    @Value("${ai.providers.deepseek.api-url}")
    private String apiUrl;

    @Override
    public ModelResponse generate(ModelRequest request) {
        ModelResponse response = new ModelResponse();
        response.setModelProvider("DEEPSEEK");
        response.setModelName(request.getModelName());

        try {
            // Implementación específica para la API de DeepSeek
            // Similar a las anteriores pero adaptado a DeepSeek

            response.setGeneratedText("Respuesta simulada de DeepSeek para: " + request.getPrompt());
            response.setTokensUsed(40); // Ejemplo
        } catch (Exception e) {
            response.setError(true);
            response.setErrorMessage("DeepSeek API Error: " + e.getMessage());
        }

        return response;
    }
}
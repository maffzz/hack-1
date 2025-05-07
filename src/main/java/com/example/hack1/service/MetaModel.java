package com.example.hack1.service;

import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MetaModel implements AIModel {

    @Value("${ai.providers.meta.api-url}")
    private String apiUrl;

    @Override
    public ModelResponse generate(ModelRequest request) {
        ModelResponse response = new ModelResponse();
        response.setModelProvider("META");
        response.setModelName(request.getModelName());

        try {
            // Implementación específica para la API de Meta
            // Similar a OpenAIModel pero adaptado a la API de Meta

            response.setGeneratedText("Respuesta simulada de Meta para: " + request.getPrompt());
            response.setTokensUsed(50); // Ejemplo
        } catch (Exception e) {
            response.setError(true);
            response.setErrorMessage("Meta API Error: " + e.getMessage());
        }

        return response;
    }
}
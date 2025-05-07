package com.example.hack1.service;

import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIModel implements AIModel {

    @Value("${ai.providers.openai.api-key}")
    private String apiKey;

    @Value("${ai.providers.openai.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OpenAIModel(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ModelResponse generate(ModelRequest request) {
        try {
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Construir el cuerpo de la solicitud según la API de OpenAI
            String requestBody = buildRequestBody(request);

            // Hacer la llamada HTTP
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Procesar la respuesta
            return processResponse(response.getBody(), request.getModelName());
        } catch (Exception e) {
            ModelResponse errorResponse = new ModelResponse();
            errorResponse.setError(true);
            errorResponse.setErrorMessage("OpenAI API Error: " + e.getMessage());
            return errorResponse;
        }
    }

    private String buildRequestBody(ModelRequest request) {
        // Implementación específica para la API de OpenAI
        if (request.isMultimodal()) {
            // Lógica para solicitudes multimodales (ej. GPT-4 Vision)
            return buildMultimodalRequest(request);
        } else {
            // Lógica para solicitudes de texto
            return buildTextRequest(request);
        }
    }

    private ModelResponse processResponse(String apiResponse, String modelName) {
        // Parsear la respuesta específica de OpenAI
        ModelResponse response = new ModelResponse();
        response.setModelProvider("OPENAI");
        response.setModelName(modelName);
        // Extraer el texto generado y los tokens usados del JSON de respuesta
        // Implementación real dependerá de la estructura de respuesta de la API
        return response;
    }
}
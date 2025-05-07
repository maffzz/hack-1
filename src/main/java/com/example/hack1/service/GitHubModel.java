package com.example.hack1.service;

import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class GitHubModel implements AIModel {

    @Value("${ai.providers.github.api-key}")
    private String apiKey;

    @Value("${ai.providers.github.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public GitHubModel(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ModelResponse generate(ModelRequest request) {
        ModelResponse response = new ModelResponse();
        response.setModelProvider("GITHUB");
        response.setModelName(request.getModelName());

        try {
            // 1. Preparar la solicitud
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.set("Accept", "application/vnd.github.v3+json");

            // 2. Construir el cuerpo según el tipo de solicitud
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", request.getModelName());
            requestBody.put("prompt", request.getPrompt());

            if (request.isMultimodal()) {
                requestBody.put("files", request.getFiles());
            }

            // 3. Configurar parámetros de la solicitud
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/predictions");

            // 4. Hacer la llamada HTTP
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> apiResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 5. Procesar la respuesta
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = apiResponse.getBody();
                return parseGitHubResponse(responseBody, request.getModelName());
            } else {
                throw new RuntimeException("GitHub API returned status: " + apiResponse.getStatusCode());
            }
        } catch (Exception e) {
            response.setError(true);
            response.setErrorMessage("GitHub Models Error: " + e.getMessage());
            return response;
        }
    }

    private ModelResponse parseGitHubResponse(Map<String, Object> apiResponse, String modelName) {
        ModelResponse response = new ModelResponse();
        response.setModelProvider("GITHUB");
        response.setModelName(modelName);

        // Asumiendo que la respuesta sigue este formato
        response.setGeneratedText((String) apiResponse.get("generated_text"));
        response.setTokensUsed(((Number) apiResponse.getOrDefault("tokens_used", 0)).intValue());

        return response;
    }

    // Método para verificar la disponibilidad del modelo
    public boolean isModelAvailable(String modelName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.set("Accept", "application/vnd.github.v3+json");

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/models/" + modelName);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}
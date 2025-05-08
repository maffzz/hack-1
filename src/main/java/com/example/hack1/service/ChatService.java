package com.example.hack1.service;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ChatService {

    @Value("${GITHUB_TOKEN}")
    private String github_token;

    public ResponseEntity<String> createRequest(String model, String requestText) {

        String endpoint = "https://models.github.ai/inference";

        ChatCompletionsClient client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(github_token))
                .endpoint(endpoint)
                .buildClient();

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(
                Collections.singletonList(new ChatRequestUserMessage(requestText))
        );
        chatCompletionsOptions.setModel(model);

        ChatCompletions completions = client.complete(chatCompletionsOptions);

        String response = completions.getChoices().get(0).getMessage().getContent();

        return ResponseEntity.ok(response);

    }

}
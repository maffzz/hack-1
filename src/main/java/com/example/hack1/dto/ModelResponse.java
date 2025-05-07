package com.example.hack1.dto;

import lombok.Data;

@Data
public class ModelResponse {
    private String generatedText;
    private int tokensUsed;
    private String modelProvider;
    private String modelName;
    private boolean error;
    private String errorMessage;
}
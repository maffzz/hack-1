package com.example.hack1.domain;

import lombok.Data;

@Data
public class AIResponse {
    private String text;               // Texto generado por el modelo
    private int tokensUsed;            // Tokens consumidos (para control de costos)
    private String modelProvider;      // Ej: "OpenAI"
    private String modelName;          // Ej: "gpt-4"
    private boolean isError;           // Indica si fue una respuesta fallida
    private String errorMessage;       // Mensaje de error (si aplica)
}
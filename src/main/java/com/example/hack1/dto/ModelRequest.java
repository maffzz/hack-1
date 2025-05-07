package com.example.hack1.dto;

import lombok.Data;
import java.util.List;

@Data
public class ModelRequest {
    private String prompt;
    private String modelName;
    private boolean multimodal;
    private List<String> files;
} // Lista de archivos en base64 para modelos multimodales

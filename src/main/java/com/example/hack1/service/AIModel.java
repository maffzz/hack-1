package com.example.hack1.service;

import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;

public interface AIModel {
    ModelResponse generate(ModelRequest request);
}
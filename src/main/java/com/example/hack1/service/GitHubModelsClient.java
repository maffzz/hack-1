package com.example.hack1.service;

import com.example.hack1.dto.ModelRequest;
import com.example.hack1.dto.ModelResponse;
import org.springframework.stereotype.Service;

@Service
public class GitHubModelsClient {

    public ModelResponse callOpenAIModel(ModelRequest request) {
        // Implementación para llamar a modelos de OpenAI a través de GitHub Models
        OpenAIModel model = new OpenAIModel(request.getModelName());
        return model.generate(request.getPrompt(), request.isMultimodal(), request.getFiles());
    }

    public ModelResponse callMetaModel(ModelRequest request) {
        // Implementación para llamar a modelos de Meta a través de GitHub Models
        MetaModel model = new MetaModel(request.getModelName());
        return model.generate(request.getPrompt(), request.isMultimodal(), request.getFiles());
    }

    public ModelResponse callDeepSeekModel(ModelRequest request) {
        // Implementación para llamar a modelos de DeepSeek a través de GitHub Models
        DeepSeekModel model = new DeepSeekModel(request.getModelName());
        return model.generate(request.getPrompt(), request.isMultimodal(), request.getFiles());
    }

    public ModelResponse callGitHubModel(ModelRequest request) {
        // Implementación para llamar a modelos nativos de GitHub
        GitHubModel model = new GitHubModel(request.getModelName());
        return model.generate(request.getPrompt(), request.isMultimodal(), request.getFiles());
    }
}
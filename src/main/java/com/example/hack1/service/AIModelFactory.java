package com.example.hack1.service;

import org.springframework.stereotype.Component;

@Component
public class AIModelFactory {

    private final OpenAIModel openAIModel;
    private final MetaModel metaModel;
    private final DeepSeekModel deepSeekModel;
    private final GitHubModel gitHubModel;

    public AIModelFactory(OpenAIModel openAIModel,
                          MetaModel metaModel,
                          DeepSeekModel deepSeekModel,
                          GitHubModel gitHubModel) {
        this.openAIModel = openAIModel;
        this.metaModel = metaModel;
        this.deepSeekModel = deepSeekModel;
        this.gitHubModel = gitHubModel;
    }

    public AIModel getModel(String provider) {
        return switch (provider.toUpperCase()) {
            case "OPENAI" -> openAIModel;
            case "META" -> metaModel;
            case "DEEPSEEK" -> deepSeekModel;
            case "GITHUB" -> gitHubModel;
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}
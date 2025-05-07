package com.example.hack1.controller;

import com.example.hack1.domain.AIRequest;
import com.example.hack1.dto.AIResponse;
import com.example.hack1.service.AIService;
import com.example.hack1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public AIResponse chatRequest(@AuthenticationPrincipal User user, @RequestBody AIRequest request) {
        request.setModelProvider("OPENAI"); // Default to OpenAI for chat
        return aiService.processRequest(user, request);
    }

    @PostMapping("/completion")
    public AIResponse completionRequest(@AuthenticationPrincipal User user, @RequestBody AIRequest request) {
        return aiService.processRequest(user, request);
    }

    @PostMapping("/multimodal")
    public AIResponse multimodalRequest(@AuthenticationPrincipal User user, @RequestBody AIRequest request) {
        request.setMultimodal(true);
        return aiService.processRequest(user, request);
    }

    @GetMapping("/models")
    public List<String> getAvailableModels(@AuthenticationPrincipal User user) {
        return aiService.getAvailableModels(user);
    }

    @GetMapping("/history")
    public List<AIRequest> getUserHistory(@AuthenticationPrincipal User user) {
        return aiService.getUserHistory(user);
    }
}
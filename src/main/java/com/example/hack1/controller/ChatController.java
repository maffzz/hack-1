package com.example.hack1.controller;

import com.example.hack1.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@PreAuthorize("hasRole('ROLE_USER')")
public class ChatController {

    @Autowired
    private ChatService aiService;

    @PostMapping("/chat")
    public ResponseEntity<String> chatAi(@RequestParam(defaultValue = "meta/Llama-4-Scout-17B-16E-Instruct") String model, @RequestBody String message) {
        return aiService.createRequest(model, message);
    }

    @PostMapping("/completion")
    public ResponseEntity<String> completionAi(@RequestParam(defaultValue = "meta/Llama-4-Scout-17B-16E-Instruct") String model, @RequestBody String message) {
        return aiService.createRequest(model, message);
    }

    @PostMapping("/multimodal")
    public ResponseEntity<String> multimodalAi(@RequestParam(defaultValue = "meta/Llama-4-Scout-17B-16E-Instruct") String model, @RequestBody String message) {
        return aiService.createRequest(model, message);
    }
}
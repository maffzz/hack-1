package com.example.hack1.controller;

import com.example.hack1.dto.ChatRequestDto;
import com.example.hack1.dto.ChatResponseDto;
import com.example.hack1.service.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AiService aiService;

    @Test
    void chat() throws Exception {
        when(aiService.chat(any(ChatRequestDto.class)))
                .thenReturn(new ChatResponseDto("Hola"));

        mvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"model":"openai/gpt-4","prompt":"Hola"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hola"));
    }

    // Agrega tests para /completion y /multimodal
}
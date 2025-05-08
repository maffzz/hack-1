package com.example.hack1.service;

import com.example.hack1.client.GitHubModelsClient;
import com.example.hack1.dto.ChatRequestDto;
import com.example.hack1.dto.ChatResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @Mock
    private GitHubModelsClient client;

    @InjectMocks
    private AiService service;

    @Test
    void chat() {
        when(client.chat("gpt-4", "Hola")).thenReturn("Mundo");

        ChatResponseDto resp = service.chat(new ChatRequestDto("gpt-4","Hola"));
        assertThat(resp.getResponse()).isEqualTo("Mundo");
    }

    // Tests para completion y multimodal...
}
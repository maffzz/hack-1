package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class UserLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String modelProvider; // "OPENAI", "META", "DEEPSEEK", "GITHUB"
    private Integer tokenLimit; // Límite de tokens para este usuario
    private Integer tokensUsed; // Tokens usados por este usuario
    private LocalDate limitDate; // Fecha para el cálculo del límite diario
}
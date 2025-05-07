package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ModelRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String modelProvider; // "OPENAI", "META", "DEEPSEEK", "GITHUB"
    private boolean allowed;
    private Integer dailyTokenLimit; // Límite diario de tokens por compañía
}
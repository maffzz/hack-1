package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ModelRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelProvider;  // OpenAI, Claude, Meta, etc.
    private String modelName;

    private int maxRequestsPerWindow;
    private int maxTokensPerWindow;
    private int windowMinutes;  // Ventana de tiempo en minutos

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}

package com.example.hack1.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AIRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String queryText;

    @Lob
    private String responseText;

    private int tokensConsumed;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String modelProvider;
    private String modelName;

    @Column(columnDefinition = "JSON")
    private String multimodalDetails;
}

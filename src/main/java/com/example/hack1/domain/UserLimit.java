package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UserLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String modelProvider;
    private String modelName;

    private int currentRequests;
    private int currentTokens;
    private LocalDateTime lastReset;
    private int windowMinutes;
}

package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restriccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private Integer limiteMensual;  // ejemplo

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;}
package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "restricciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restriccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelo;

    @Column(name = "limite_mensual", nullable = false)
    private Integer limiteMensual;

    @Column(name = "tokens_maximos")
    private Integer tokensMaximos;

    @Column(name = "activa")
    private boolean activa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "proveedor_ia", nullable = false)
    private ProveedorIA proveedorIA;

    public enum ProveedorIA {
        OPENAI,
        META,
        DEEPSEEK,
        GITHUB,
        CLAUDE
    }
}
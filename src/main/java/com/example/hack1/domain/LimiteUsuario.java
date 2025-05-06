package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimiteUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private Integer limite;
    private String ventanaTiempo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
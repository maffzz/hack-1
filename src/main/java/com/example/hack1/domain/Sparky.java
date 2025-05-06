package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sparkies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sparky {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "sparky", cascade = CascadeType.ALL)
    private List<Empresa> empresas;

    @OneToMany(mappedBy = "sparky", cascade = CascadeType.ALL)
    private List<Usuario> administradores;
}
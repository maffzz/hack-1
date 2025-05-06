package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<LimiteUsuario> limites;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SolicitudIA> solicitudes;}

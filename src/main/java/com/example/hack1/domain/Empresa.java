package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ruc;
    private LocalDate fechaAfiliacion;
    private boolean activa;

    @OneToOne
    private Usuario administrador;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Restriccion> restricciones;

    @ManyToOne
    @JoinColumn(name = "sparky_id")
    private Sparky sparky;}
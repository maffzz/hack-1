package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(name = "fecha_afiliacion", nullable = false)
    private LocalDate fechaAfiliacion;

    @Column(nullable = false)
    private boolean activa = true;

    @Column(name = "creditos_disponibles")
    private Integer creditosDisponibles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private Usuario administrador;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Restriccion> restricciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sparky_id", nullable = false)
    private Sparky sparky;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<ConsumoEmpresa> consumos;
}
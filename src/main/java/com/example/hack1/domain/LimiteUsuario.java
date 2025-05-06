package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "limites_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimiteUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer limite;

    @Enumerated(EnumType.STRING)
    @Column(name = "ventana_tiempo", nullable = false)
    private VentanaTiempo ventanaTiempo;

    @Column(name = "tokens_maximos")
    private Integer tokensMaximos;

    @Column(name = "consumido")
    private Integer consumido = 0;

    @Column(name = "ultimo_reset")
    private LocalDateTime ultimoReset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public enum VentanaTiempo {
        MINUTO(ChronoUnit.MINUTES),
        HORA(ChronoUnit.HOURS),
        DIA(ChronoUnit.DAYS),
        SEMANA(ChronoUnit.WEEKS),
        MES(ChronoUnit.MONTHS);

        private final ChronoUnit unidad;

        VentanaTiempo(ChronoUnit unidad) {
            this.unidad = unidad;
        }

        public ChronoUnit getUnidad() {
            return unidad;
        }
    }

    public boolean necesitaReset() {
        if (ultimoReset == null) return true;
        return ultimoReset.plus(1, ventanaTiempo.getUnidad()).isBefore(LocalDateTime.now());
    }

    public void resetearConsumo() {
        this.consumido = 0;
        this.ultimoReset = LocalDateTime.now();
    }
}
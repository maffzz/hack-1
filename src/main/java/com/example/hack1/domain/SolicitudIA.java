package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_ia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String consulta;

    @Column(columnDefinition = "TEXT")
    private String respuesta;

    @Column(name = "tokens_consumidos", nullable = false)
    private Integer tokensConsumidos;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_solicitud", nullable = false)
    private TipoSolicitud tipoSolicitud;

    @Column(name = "multimedia_ref")
    private String multimediaRef;

    @Column(nullable = false)
    private boolean exito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public enum TipoSolicitud {
        CHAT,
        COMPLETION,
        MULTIMODAL
    }
}
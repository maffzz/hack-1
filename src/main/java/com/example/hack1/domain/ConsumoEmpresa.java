package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumo_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", unique = true)
    private Empresa empresa;

    @Column(name = "tokens_consumidos_mes", nullable = false)
    private Integer tokensConsumidosMes = 0;

    @Column(name = "solicitudes_mes", nullable = false)
    private Integer solicitudesMes = 0;

    @Column(name = "creditos_consumidos", nullable = false)
    private Integer creditosConsumidos = 0;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Version
    private Integer version; // Para control de concurrencia

    // Método para actualizar consumos
    public void registrarConsumo(Integer tokensConsumidos) {
        this.tokensConsumidosMes += tokensConsumidos;
        this.solicitudesMes += 1;
        this.creditosConsumidos += calcularCreditos(tokensConsumidos);
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Lógica de conversión tokens-créditos (ejemplo)
    private Integer calcularCreditos(Integer tokens) {
        return tokens / 1000; // 1 crédito = 1000 tokens
    }
}
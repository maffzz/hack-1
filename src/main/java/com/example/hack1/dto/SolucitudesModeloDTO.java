package com.example.hack1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudesModeloDTO {
    private String modelo;
    private Integer totalSolicitudes;
    private Integer tokensConsumidos;
    private LocalDateTime primeraSolicitud;
    private LocalDateTime ultimaSolicitud;
    private Double promedioTokensPorSolicitud;

    public static SolicitudesModeloDTO of(String modelo, Integer totalSolicitudes, Integer tokensConsumidos,
                                          LocalDateTime primera, LocalDateTime ultima) {
        double promedio = totalSolicitudes > 0 ? (double) tokensConsumidos / totalSolicitudes : 0.0;

        return new SolicitudesModeloDTO(
                modelo,
                totalSolicitudes,
                tokensConsumidos,
                primera,
                ultima,
                promedio
        );
    }
}
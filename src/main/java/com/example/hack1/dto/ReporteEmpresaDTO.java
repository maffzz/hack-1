package com.example.hack1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteEmpresaDTO {
    private Long empresaId;
    private String nombreEmpresa;
    private boolean activa;
    private Integer creditosDisponibles;
    private Integer creditosConsumidos;
    private Map<String, Integer> tokensPorModelo;  // Modelo -> Tokens consumidos
    private Map<String, Integer> solicitudesPorModelo; // Modelo -> Número de solicitudes
    private String periodoReporte; // Ej: "Enero 2023", "Últimos 30 días", etc.
}
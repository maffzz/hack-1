package com.example.hack1.dto;

import com.example.hack1.domain.LimiteUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoDTO {
    private int totalTokens;
    private Map<String, Integer> tokensPorModelo;
    private String modelo;
    private int consumido;
    private int limite;
    private int tokensConsumidos;
    private int tokensMaximos;
    private String ventanaTiempo;
    private LocalDateTime ultimoReset;

    public ConsumoDTO(String modelo, Integer consumido, Integer limite, Integer tokensMaximos, LimiteUsuario.VentanaTiempo ventanaTiempo, LocalDateTime ultimoReset) {
    }
}
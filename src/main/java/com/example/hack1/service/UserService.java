package com.example.hack1.service;

import com.example.hack1.domain.*;
import com.example.hack1.dto.ConsumoDTO;
import com.example.hack1.exception.NotFound;
import com.example.hack1.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsuarioRepository usuarioRepository;
    private final SolicitudIARepository solicitudIARepository;
    private final LimiteUsuarioRepository limiteUsuarioRepository;

    @Transactional(readOnly = true)
    public ConsumoDTO obtenerConsumoUsuario(Long usuarioId, LocalDateTime desde, LocalDateTime hasta) {
        List<SolicitudIA> solicitudes = (desde != null && hasta != null) ?
                solicitudIARepository.findByUsuarioIdAndFechaHoraBetween(usuarioId, desde, hasta) :
                solicitudIARepository.findByUsuarioId(usuarioId);

        Map<String, Integer> tokensPorModelo = solicitudes.stream()
                .collect(Collectors.groupingBy(SolicitudIA::getModelo,
                        Collectors.summingInt(SolicitudIA::getTokensConsumidos)));

        int total = tokensPorModelo.values().stream().mapToInt(Integer::intValue).sum();

        return new ConsumoDTO(total, tokensPorModelo);
    }

    @Transactional(readOnly = true)
    public List<SolicitudIA> obtenerHistorialSolicitudes(Long usuarioId, String modelo, LocalDateTime desde, LocalDateTime hasta) {
        if (modelo != null && desde != null && hasta != null) {
            return solicitudIARepository.findByUsuarioIdAndModeloAndFechaHoraBetween(
                    usuarioId, modelo, desde, hasta);
        } else if (modelo != null) {
            return solicitudIARepository.findByUsuarioIdAndModelo(usuarioId, modelo);
        } else if (desde != null && hasta != null) {
            return solicitudIARepository.findByUsuarioIdAndFechaHoraBetween(usuarioId, desde, hasta);
        }
        return solicitudIARepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<LimiteUsuario> obtenerLimitesUsuario(Long usuarioId) {
        return limiteUsuarioRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerInformacionUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFound("Usuario no encontrado"));
    }
}
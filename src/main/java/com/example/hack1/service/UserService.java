package com.example.hack1.service;

import com.example.hack1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsuarioRepository usuarioRepository;
    private final SolicitudIARepository solicitudIARepository;

    public ConsumoDTO obtenerConsumoUsuario(Long usuarioId) {
        List<SolicitudIA> solicitudes = solicitudIARepository.findByUsuarioId(usuarioId);
        Map<String, Integer> tokensPorModelo = solicitudes.stream()
                .collect(Collectors.groupingBy(SolicitudIA::getModelo,
                        Collectors.summingInt(SolicitudIA::getTokensConsumidos)));
        int total = tokensPorModelo.values().stream().mapToInt(Integer::intValue).sum();
        return new ConsumoDTO(total, tokensPorModelo);
    }

    public List<SolicitudIA> obtenerHistorialSolicitudes(Long usuarioId) {
        return solicitudIARepository.findByUsuarioId(usuarioId);
    }

}
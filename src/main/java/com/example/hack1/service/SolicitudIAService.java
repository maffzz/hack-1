package com.example.hack1.service;

import com.example.hack1.repository.SolicitudIARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitudIAService {
    private final SolicitudIARepository solicitudIARepository;
    private final UsuarioRepository usuarioRepository;
    private final LimiteService limiteService;

    public SolicitudIA procesarConsulta(Long usuarioId, String modelo, String consulta, int tokensConsumidos, String respuesta, String archivo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!limiteService.puedeConsumir(usuario, modelo, tokensConsumidos)) {
            throw new IllegalStateException("Límite excedido para el modelo " + modelo);
        }

        SolicitudIA solicitud = SolicitudIA.builder()
                .usuario(usuario)
                .modelo(modelo)
                .consulta(consulta)
                .tokensConsumidos(tokensConsumidos)
                .respuesta(respuesta)
                .nombreArchivo(archivo)
                .fechaHora(LocalDateTime.now())
                .build();

        limiteService.registrarConsumo(usuario, modelo, tokensConsumidos);

        return solicitudIARepository.save(solicitud);
    }

    public List<String> obtenerModelosDisponibles(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return usuario.getLimites().stream()
                .map(Limite::getModelo)
                .distinct()
                .collect(Collectors.toList());
    }

}

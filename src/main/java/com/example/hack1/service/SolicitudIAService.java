package com.example.hack1.service;

import com.example.hack1.domain.*;
import com.example.hack1.exception.EntityNotFoundException;
import com.example.hack1.exception.LimiteExcedidoException;
import com.example.hack1.repository.SolicitudIARepository;
import com.example.hack1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudIAService {
    private final SolicitudIARepository solicitudIARepository;
    private final UsuarioRepository usuarioRepository;
    private final LimiteService limiteService;
    private final EmpresaRepository empresaRepository;

    @Transactional
    public SolicitudIA procesarConsulta(Long usuarioId, String modelo, String consulta,
                                        int tokensConsumidos, String respuesta,
                                        String archivo, SolicitudIA.TipoSolicitud tipo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Empresa empresa = usuario.getEmpresa();
        if (empresa == null || !empresa.isActiva()) {
            throw new IllegalStateException("Empresa no activa o no asignada");
        }

        // Verificar límites
        if (!limiteService.verificarLimitesUsuario(usuario, modelo, tokensConsumidos)) {
            throw new LimiteExcedidoException("Límite excedido para el modelo " + modelo);
        }

        // Registrar solicitud
        SolicitudIA solicitud = SolicitudIA.builder()
                .usuario(usuario)
                .empresa(empresa)
                .modelo(modelo)
                .consulta(consulta)
                .respuesta(respuesta)
                .tokensConsumidos(tokensConsumidos)
                .fechaHora(LocalDateTime.now())
                .tipoSolicitud(tipo)
                .multimediaRef(archivo)
                .exito(true)
                .build();

        // Actualizar consumos
        limiteService.actualizarConsumos(usuario, modelo, tokensConsumidos);
        empresaRepository.actualizarCreditos(empresa.getId(), tokensConsumidos);

        return solicitudIARepository.save(solicitud);
    }

    public List<String> obtenerModelosDisponibles(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Modelos de restricciones de empresa + límites específicos del usuario
        List<String> modelosEmpresa = usuario.getEmpresa().getRestricciones().stream()
                .filter(Restriccion::isActiva)
                .map(Restriccion::getModelo)
                .distinct()
                .collect(Collectors.toList());

        List<String> modelosUsuario = usuario.getLimites().stream()
                .map(LimiteUsuario::getModelo)
                .distinct()
                .collect(Collectors.toList());

        // Combinar ambos listados sin duplicados
        modelosUsuario.addAll(modelosEmpresa);
        return modelosUsuario.stream().distinct().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudIA> obtenerHistorialUsuario(Long usuarioId, String modelo, LocalDateTime desde, LocalDateTime hasta) {
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
}
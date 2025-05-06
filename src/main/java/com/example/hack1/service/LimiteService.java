package com.example.hack1.service;

import com.example.hack1.domain.*;
import com.example.hack1.dto.ConsumoDTO;
import com.example.hack1.exception.Conflict;
import com.example.hack1.exception.NotFound;
import com.example.hack1.repository.LimiteUsuarioRepository;
import com.example.hack1.repository.RestriccionRepository;
import com.example.hack1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LimiteService {
    private final LimiteUsuarioRepository limiteUsuarioRepository;
    private final RestriccionRepository restriccionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public boolean verificarLimitesUsuario(Usuario usuario, String modelo, int tokensSolicitados) {
        // 1. Verificar límites específicos del usuario
        List<LimiteUsuario> limitesUsuario = limiteUsuarioRepository.findByUsuarioIdAndModelo(usuario.getId(), modelo);

        for (LimiteUsuario limite : limitesUsuario) {
            // Resetear límites si la ventana de tiempo ha expirado
            if (limite.necesitaReset()) {
                limite.resetearConsumo();
                limiteUsuarioRepository.save(limite);
            }

            // Verificar límite de solicitudes
            if (limite.getLimite() != null && limite.getConsumido() >= limite.getLimite()) {
                throw new Conflict(
                        String.format("Límite de solicitudes alcanzado (%d/%d) para el modelo %s",
                                limite.getConsumido(), limite.getLimite(), modelo)
                );
            }

            // Verificar límite de tokens
            if (limite.getTokensMaximos() != null &&
                    (limite.getConsumido() + tokensSolicitados) > limite.getTokensMaximos()) {
                throw new Conflict(
                        String.format("Límite de tokens alcanzado (%d + %d > %d) para el modelo %s",
                                limite.getConsumido(), tokensSolicitados, limite.getTokensMaximos(), modelo)
                );
            }
        }

        // 2. Verificar restricciones de la empresa
        if (usuario.getEmpresa() != null) {
            List<Restriccion> restricciones = restriccionRepository
                    .findByEmpresaIdAndModelo(usuario.getEmpresa().getId(), modelo);

            for (Restriccion restriccion : restricciones) {
                if (!restriccion.isActiva()) {
                    throw new Conflict(
                            "El modelo " + modelo + " está desactivado para su empresa");
                }
            }
        }

        return true;
    }

    /**
     * Registra el consumo de tokens y solicitudes para un usuario
     */
    @Transactional
    public void actualizarConsumos(Usuario usuario, String modelo, int tokensConsumidos) {
        List<LimiteUsuario> limites = limiteUsuarioRepository.findByUsuarioIdAndModelo(usuario.getId(), modelo);

        for (LimiteUsuario limite : limites) {
            if (limite.necesitaReset()) {
                limite.resetearConsumo();
            }

            limite.setConsumido(limite.getConsumido() + 1);
            if (tokensConsumidos > 0) {
                limite.setConsumido(limite.getConsumido() + tokensConsumidos);
            }

            limiteUsuarioRepository.save(limite);
        }
    }

    /**
     * Crea o actualiza un límite para un usuario
     */
    @Transactional
    public LimiteUsuario asignarLimiteUsuario(Long usuarioId, LimiteUsuario limiteRequest) {
        LimiteUsuario limite = limiteUsuarioRepository
                .findByUsuarioIdAndModelo(usuarioId, limiteRequest.getModelo())
                .stream()
                .findFirst()
                .orElse(limiteRequest);

        limite.setLimite(limiteRequest.getLimite());
        limite.setTokensMaximos(limiteRequest.getTokensMaximos());
        limite.setVentanaTiempo(limiteRequest.getVentanaTiempo());
        limite.setUltimoReset(LocalDateTime.now());
        limite.setConsumido(0);
        limite.setConsumido(0);

        if (limite.getUsuario() == null) {
            limite.setUsuario(usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new NotFound("Usuario no encontrado")));
        }

        return limiteUsuarioRepository.save(limite);
    }

    /**
     * Obtiene los límites actuales de un usuario
     */
    @Transactional(readOnly = true)
    public List<LimiteUsuario> obtenerLimitesUsuario(Long usuarioId) {
        return limiteUsuarioRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Reinicia los contadores de consumo para un usuario
     */
    @Transactional
    public void reiniciarConsumos(Long usuarioId) {
        List<LimiteUsuario> limites = limiteUsuarioRepository.findByUsuarioId(usuarioId);
        limites.forEach(limite -> {
            limite.setConsumido(0);
            limite.setConsumido(0);
            limite.setUltimoReset(LocalDateTime.now());
            limiteUsuarioRepository.save(limite);
        });
    }

    /**
     * Obtiene el consumo actual de un usuario para un modelo específico
     */
    @Transactional(readOnly = true)
    public ConsumoDTO obtenerConsumoActual(Long usuarioId, String modelo) {
        return limiteUsuarioRepository.findByUsuarioIdAndModelo(usuarioId, modelo)
                .stream()
                .findFirst()
                .map(limite -> new ConsumoDTO(
                        limite.getModelo(),
                        limite.getConsumido(),
                        limite.getLimite(),
                        limite.getTokensMaximos(),
                        limite.getVentanaTiempo(),
                        limite.getUltimoReset()
                ))
                .orElseThrow(() -> new NotFound("No se encontraron límites para el modelo especificado"));
    }
}
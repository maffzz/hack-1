package com.example.hack1.service;

import com.example.hack1.domain.*;
import com.example.hack1.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final RestriccionRepository restriccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final LimiteUsuarioRepository limiteUsuarioRepository;
    private final SolicitudIARepository solicitudIARepository;
    private final ConsumoEmpresaRepository consumoEmpresaRepository;

    @Transactional
    public Restriccion crearRestriccion(Long empresaId, Restriccion restriccion) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
        restriccion.setEmpresa(empresa);
        return restriccionRepository.save(restriccion);
    }

    public List<Restriccion> listarRestricciones(Long empresaId) {
        return restriccionRepository.findByEmpresaId(empresaId);
    }

    @Transactional
    public Restriccion actualizarRestriccion(Long restriccionId, Restriccion datos) {
        Restriccion restriccion = restriccionRepository.findById(restriccionId)
                .orElseThrow(() -> new EntityNotFoundException("Restricción no encontrada"));
        restriccion.setModelo(datos.getModelo());
        restriccion.setLimiteMensual(datos.getLimiteMensual());
        restriccion.setTokensMaximos(datos.getTokensMaximos());
        restriccion.setActiva(datos.isActiva());
        return restriccionRepository.save(restriccion);
    }

    @Transactional
    public void eliminarRestriccion(Long restriccionId) {
        restriccionRepository.deleteById(restriccionId);
    }

    @Transactional
    public Usuario crearUsuario(Long empresaId, Usuario usuario) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
        usuario.setEmpresa(empresa);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios(Long empresaId) {
        return usuarioRepository.findByEmpresaId(empresaId);
    }

    public Usuario obtenerUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public Usuario actualizarUsuario(Long usuarioId, Usuario datos) {
        Usuario usuario = obtenerUsuario(usuarioId);
        usuario.setNombreCompleto(datos.getNombreCompleto());
        usuario.setCorreo(datos.getCorreo());
        usuario.setRole(datos.getRole());
        usuario.setEnabled(datos.isEnabled());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public LimiteUsuario asignarLimiteUsuario(Long usuarioId, LimiteUsuario limite) {
        Usuario usuario = obtenerUsuario(usuarioId);
        limite.setUsuario(usuario);
        return limiteUsuarioRepository.save(limite);
    }

    @Transactional
    public SolicitudIA registrarSolicitudIA(SolicitudIA solicitud) {
        // Verificar límites antes de registrar
        verificarLimitesUsuario(solicitud.getUsuario(), solicitud.getModelo(), solicitud.getTokensConsumidos());
        return solicitudIARepository.save(solicitud);
    }

    private void verificarLimitesUsuario(Usuario usuario, String modelo, int tokensConsumidos) {
        List<LimiteUsuario> limites = limiteUsuarioRepository.findByUsuarioIdAndModelo(usuario.getId(), modelo);

        for (LimiteUsuario limite : limites) {
            if (limite.necesitaReset()) {
                limite.resetearConsumo();
            }

            if (limite.getLimite() != null && limite.getConsumido() >= limite.getLimite()) {
                throw new LimiteExcedidoException("Límite de solicitudes alcanzado para el modelo " + modelo);
            }

            if (limite.getTokensMaximos() != null &&
                    (limite.get
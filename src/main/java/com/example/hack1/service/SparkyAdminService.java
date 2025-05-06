package com.example.hack1.service;

import com.example.hack1.domain.*;

import com.example.hack1.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SparkyAdminService {
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudIARepository solicitudIARepository;
    private final ConsumoEmpresaRepository consumoEmpresaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Empresa crearEmpresa(Empresa empresa, Usuario administrador) {
        if (empresaRepository.existsByRuc(empresa.getRuc())) {
            throw new IllegalStateException("Ya existe una empresa con este RUC");
        }

        administrador.setPassword(passwordEncoder.encode(administrador.getPassword()));
        administrador.setRole(Role.ROLE_COMPANY_ADMIN);
        administrador.setEmpresa(empresa);
        administrador = usuarioRepository.save(administrador);

        empresa.setAdministrador(administrador);
        empresa.setActiva(true);
        empresa.setFechaAfiliacion(LocalDate.now());

        return empresaRepository.save(empresa);
    }

    @Transactional(readOnly = true)
    public List<Empresa> listarEmpresas(boolean soloActivas) {
        return soloActivas ?
                empresaRepository.findByActivaTrue() :
                empresaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empresa obtenerEmpresaPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    @Transactional
    public Empresa actualizarEmpresa(Long id, Empresa actualizada) {
        Empresa empresa = obtenerEmpresaPorId(id);
        empresa.setNombre(actualizada.getNombre());
        empresa.setRuc(actualizada.getRuc());
        empresa.setFechaAfiliacion(actualizada.getFechaAfiliacion());
        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa actualizarEstadoEmpresa(Long id, boolean activa) {
        Empresa empresa = obtenerEmpresaPorId(id);
        empresa.setActiva(activa);
        return empresaRepository.save(empresa);
    }

    @Transactional(readOnly = true)
    public ConsumoDTO obtenerConsumoEmpresa(Long empresaId, LocalDateTime desde, LocalDateTime hasta) {
        List<SolicitudIA> solicitudes = (desde != null && hasta != null) ?
                solicitudIARepository.findByUsuarioEmpresaIdAndFechaHoraBetween(empresaId, desde, hasta) :
                solicitudIARepository.findByUsuarioEmpresaId(empresaId);

        Map<String, Integer> tokensPorModelo = solicitudes.stream()
                .collect(Collectors.groupingBy(SolicitudIA::getModelo,
                        Collectors.summingInt(SolicitudIA::getTokensConsumidos)));

        int total = tokensPorModelo.values().stream().mapToInt(Integer::intValue).sum();

        return new ConsumoDTO(total, tokensPorModelo);
    }

    @Transactional(readOnly = true)
    public List<ReporteEmpresaDTO> generarReporteConsumoGlobal() {
        return empresaRepository.findAll().stream()
                .map(empresa -> {
                    ConsumoDTO consumo = obtenerConsumoEmpresa(empresa.getId(), null, null);
                    return new ReporteEmpresaDTO(
                            empresa.getId(),
                            empresa.getNombre(),
                            empresa.isActiva(),
                            consumo.getTotal(),
                            consumo.getTokensPorModelo()
                    );
                })
                .collect(Collectors.toList());
    }
}
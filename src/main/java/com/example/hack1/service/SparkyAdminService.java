package com.example.hack1.service;


import com.example.hack1.domain.Empresa;
import com.example.hack1.domain.SolicitudIA;
import com.example.hack1.domain.Usuario;
import com.example.hack1.repository.EmpresaRepository;
import com.example.hack1.repository.SolicitudIARepository;
import com.example.hack1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SparkyAdminService {
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudIARepository solicitudIARepository;

    public Empresa crearEmpresa(Empresa empresa, Usuario administrador) {
        administrador.setEmpresa(empresa);
        empresa.setAdministrador(administrador);
        empresa.setActiva(true);
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa obtenerEmpresaPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    public Empresa actualizarEmpresa(Long id, Empresa actualizada) {
        Empresa empresa = obtenerEmpresaPorId(id);
        empresa.setNombre(actualizada.getNombre());
        empresa.setRuc(actualizada.getRuc());
        empresa.setFechaAfiliacion(actualizada.getFechaAfiliacion());
        return empresaRepository.save(empresa);
    }

    public Empresa actualizarEstadoEmpresa(Long id, boolean activa) {
        Empresa empresa = obtenerEmpresaPorId(id);
        empresa.setActiva(activa);
        return empresaRepository.save(empresa);
    }

    public ConsumoDTO obtenerConsumoEmpresa(Long empresaId) {
        List<SolicitudIA> solicitudes = solicitudIARepository.findByUsuarioEmpresaId(empresaId);
        Map<String, Integer> tokensPorModelo = solicitudes.stream()
                .collect(Collectors.groupingBy(SolicitudIA::getModelo,
                        Collectors.summingInt(SolicitudIA::getTokensConsumidos)));
        int total = tokensPorModelo.values().stream().mapToInt(Integer::intValue).sum();
        return new ConsumoDTO(total, tokensPorModelo);
    }

}


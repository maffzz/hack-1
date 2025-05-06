package com.example.hack1.repository;

import com.example.hack1.domain.SolicitudIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudIARepository extends JpaRepository<SolicitudIA, Long> {
    List<SolicitudIA> findByUsuarioId(Long usuarioId);
    List<SolicitudIA> findByUsuarioEmpresaId(Long empresaId);
}
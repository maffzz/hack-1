package com.example.hack1.repository;

import com.example.hack1.domain.SolicitudIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudIARepository extends JpaRepository<SolicitudIA, Long> {
    List<SolicitudIA> findByUsuarioId(Long usuarioId);
    List<SolicitudIA> findByUsuarioEmpresaId(Long empresaId);

    List<SolicitudIA> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<SolicitudIA> findByUsuarioIdAndFechaHoraBetween(Long usuarioId, LocalDateTime fechaHoraAfter, LocalDateTime fechaHoraBefore);

    List<SolicitudIA> findByUsuarioIdAndModelo(Long usuarioId, String modelo);

    List<SolicitudIA> findByUsuarioIdAndModeloAndFechaHoraBetween(Long usuarioId, String modelo, LocalDateTime fechaHoraAfter, LocalDateTime fechaHoraBefore);
}
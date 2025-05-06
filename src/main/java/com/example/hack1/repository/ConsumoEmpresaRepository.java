package com.example.hack1.repository;

import com.example.hack1.domain.ConsumoEmpresa;
import com.example.hack1.dto.SolicitudesModeloDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsumoEmpresaRepository extends JpaRepository<ConsumoEmpresa, Long> {

    // 1. Encontrar consumo por empresa
    Optional<ConsumoEmpresa> findByEmpresaId(Long empresaId);

    // 2. Obtener tokens consumidos por modelo en un período
    @Query("SELECT NEW com.example.hack1.dto.ConsumoModeloDTO(s.modelo, SUM(s.tokensConsumidos)) " +
            "FROM SolicitudIA s " +
            "WHERE s.empresa.id = :empresaId " +
            "AND s.fechaHora BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY s.modelo")
    List<ConsumoModeloDTO> findTokensConsumidosPorModelo(
            @Param("empresaId") Long empresaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    // 3. Contar solicitudes por modelo
    @Query("SELECT NEW com.example.hack1.dto.SolicitudesModeloDTO(s.modelo, COUNT(s)) " +
            "FROM SolicitudIA s " +
            "WHERE s.empresa.id = :empresaId " +
            "AND s.fechaHora BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY s.modelo")
    List<SolicitudesModeloDTO> countSolicitudesPorModelo(
            @Param("empresaId") Long empresaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    // 4. Actualizar créditos disponibles (usando @Modifying)
    @Modifying
    @Query("UPDATE Empresa e SET e.creditosDisponibles = e.creditosDisponibles - :tokensConsumidos " +
            "WHERE e.id = :empresaId")
    void actualizarCreditosEmpresa(
            @Param("empresaId") Long empresaId,
            @Param("tokensConsumidos") Integer tokensConsumidos);
}
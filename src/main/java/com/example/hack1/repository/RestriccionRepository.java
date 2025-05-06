package com.example.hack1.repository;

import com.example.hack1.domain.Restriccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestriccionRepository extends JpaRepository<Restriccion, Long> {
    List<Restriccion> findByEmpresaId(Long empresaId);
    Optional<Restriccion> findByEmpresaIdAndModelo(Long empresaId, String modelo);
}
package com.example.hack1.repository;

import com.example.hack1.domain.LimiteUsuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LimiteUsuarioRepository extends JpaRepository<Limite, Long> {
    List<LimiteUsuario> findByUsuarioId(Long usuarioId);
    Optional<LimiteUsuario> findByUsuarioIdAndModelo(Long usuarioId, String modelo);
}
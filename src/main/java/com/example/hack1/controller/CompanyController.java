package com.example.hack1.controller;

import com.example.hack1.domain.*;
import com.example.hack1.dto.ConsumoDTO;
import com.example.hack1.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
public class CompanyController {

    private final CompanyService companyService;

    // Restricciones endpoints
    @PostMapping("/restrictions")
    public ResponseEntity<Restriccion> crearRestriccion(
            @RequestParam Long empresaId, @RequestBody Restriccion restriccion) {
        return ResponseEntity.ok(companyService.crearRestriccion(empresaId, restriccion));
    }

    @GetMapping("/restrictions")
    public ResponseEntity<List<Restriccion>> listarRestricciones(@RequestParam Long empresaId) {
        return ResponseEntity.ok(companyService.listarRestricciones(empresaId));
    }

    @PutMapping("/restrictions/{id}")
    public ResponseEntity<Restriccion> actualizarRestriccion(
            @PathVariable Long id, @RequestBody Restriccion restriccion) {
        return ResponseEntity.ok(companyService.actualizarRestriccion(id, restriccion));
    }

    @DeleteMapping("/restrictions/{id}")
    public ResponseEntity<Void> eliminarRestriccion(@PathVariable Long id) {
        companyService.eliminarRestriccion(id);
        return ResponseEntity.noContent().build();
    }

    // Usuarios endpoints
    @PostMapping("/users")
    public ResponseEntity<Usuario> crearUsuario(
            @RequestParam Long empresaId, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(companyService.crearUsuario(empresaId, usuario));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Usuario>> listarUsuarios(@RequestParam Long empresaId) {
        return ResponseEntity.ok(companyService.listarUsuarios(empresaId));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.obtenerUsuario(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(companyService.actualizarUsuario(id, usuario));
    }

    @PostMapping("/users/{id}/limits")
    public ResponseEntity<LimiteUsuario> asignarLimiteUsuario(
            @PathVariable Long id, @RequestBody LimiteUsuario limite) {
        return ResponseEntity.ok(companyService.asignarLimiteUsuario(id, limite));
    }

    @GetMapping("/users/{id}/consumption")
    public ResponseEntity<ConsumoDTO> obtenerConsumoUsuario(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime desde,
            @RequestParam(required = false) LocalDateTime hasta) {
        return ResponseEntity.ok(companyService.obtenerConsumoUsuario(id, desde, hasta));
    }
}
package com.example.hack1.controller;

import com.example.hack1.domain.Empresa;
import com.example.hack1.domain.Usuario;

import com.example.hack1.dto.ConsumoDTO;
import com.example.hack1.service.SparkyAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_SPARKY_ADMIN')")
public class AdminController {

    private final SparkyAdminService adminService;

    @PostMapping
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa, @RequestBody Usuario administrador) {
        return ResponseEntity.ok(adminService.crearEmpresa(empresa, administrador));
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas(@RequestParam(required = false) Boolean soloActivas) {
        return ResponseEntity.ok(adminService.listarEmpresas(soloActivas != null && soloActivas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.obtenerEmpresaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        return ResponseEntity.ok(adminService.actualizarEmpresa(id, empresa));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Empresa> actualizarEstadoEmpresa(
            @PathVariable Long id, @RequestParam boolean activa) {
        return ResponseEntity.ok(adminService.actualizarEstadoEmpresa(id, activa));
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<ConsumoDTO> obtenerConsumoEmpresa(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime desde,
            @RequestParam(required = false) LocalDateTime hasta) {
        return ResponseEntity.ok(adminService.obtenerConsumoEmpresa(id, desde, hasta));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReporteEmpresaDTO>> generarReporteGlobal() {
        return ResponseEntity.ok(adminService.generarReporteConsumoGlobal());
    }
}
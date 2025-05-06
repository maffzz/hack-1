package com.example.hack1.controller;

import com.example.hack1.domain.SolicitudIA;
import com.example.hack1.service.SolicitudIAService;
import com.example.hack1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_COMPANY_ADMIN')")
public class AIController {

    private final SolicitudIAService solicitudIAService;
    private final UserService userService;

    @PostMapping("/chat")
    public ResponseEntity<SolicitudIA> procesarChat(
            @RequestParam Long usuarioId,
            @RequestParam String modelo,
            @RequestParam String consulta,
            @RequestParam int tokensConsumidos,
            @RequestParam String respuesta) {
        return ResponseEntity.ok(solicitudIAService.procesarConsulta(
                usuarioId, modelo, consulta, tokensConsumidos, respuesta, null, SolicitudIA.TipoSolicitud.CHAT));
    }

    @PostMapping("/completion")
    public ResponseEntity<SolicitudIA> procesarCompletion(
            @RequestParam Long usuarioId,
            @RequestParam String modelo,
            @RequestParam String consulta,
            @RequestParam int tokensConsumidos,
            @RequestParam String respuesta) {
        return ResponseEntity.ok(solicitudIAService.procesarConsulta(
                usuarioId, modelo, consulta, tokensConsumidos, respuesta, null, SolicitudIA.TipoSolicitud.COMPLETION));
    }

    @PostMapping("/multimodal")
    public ResponseEntity<SolicitudIA> procesarMultimodal(
            @RequestParam Long usuarioId,
            @RequestParam String modelo,
            @RequestParam String consulta,
            @RequestParam int tokensConsumidos,
            @RequestParam String respuesta,
            @RequestParam String archivo) {
        return ResponseEntity.ok(solicitudIAService.procesarConsulta(
                usuarioId, modelo, consulta, tokensConsumidos, respuesta, archivo, SolicitudIA.TipoSolicitud.MULTIMODAL));
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> obtenerModelosDisponibles(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(solicitudIAService.obtenerModelosDisponibles(usuarioId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<SolicitudIA>> obtenerHistorial(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) LocalDateTime desde,
            @RequestParam(required = false) LocalDateTime hasta) {
        return ResponseEntity.ok(userService.obtenerHistorialSolicitudes(usuarioId, modelo, desde, hasta));
    }
}
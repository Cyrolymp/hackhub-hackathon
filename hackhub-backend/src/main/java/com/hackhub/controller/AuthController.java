package com.hackhub.controller;

import com.hackhub.dto.request.LoginRequest;
import com.hackhub.dto.request.RegisterOrganisateurRequest;
import com.hackhub.dto.request.RegisterRequest;
import com.hackhub.dto.response.ApiResponse;
import com.hackhub.dto.response.AuthResponse;
import com.hackhub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    /** POST /api/auth/register — Inscription d'un participant */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest req) {
        AuthResponse resp = authService.registerParticipant(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Compte créé avec succès.", resp));
    }

    /** POST /api/auth/login — Connexion (tous rôles) */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Connexion réussie.", authService.login(req)));
    }

    /** POST /api/auth/register-organisateur — Réservé à l'admin */
    @PostMapping("/register-organisateur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AuthResponse>> registerOrganisateur(
            @Valid @RequestBody RegisterOrganisateurRequest req) {
        AuthResponse resp = authService.registerOrganisateur(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Organisateur créé avec succès.", resp));
    }
}

package com.hackhub.controller;

import com.hackhub.dto.request.CreateOrganisateurRequest;
import com.hackhub.dto.response.ApiResponse;
import com.hackhub.dto.response.AuthResponse;
import com.hackhub.dto.response.UserSummaryResponse;
import com.hackhub.entity.Role;
import com.hackhub.entity.User;
import com.hackhub.exception.ResourceNotFoundException;
import com.hackhub.repository.UserRepository;
import com.hackhub.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Gestion des utilisateurs et organisateurs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/organisateurs")
    @Operation(summary = "Créer un compte organisateur")
    public ResponseEntity<ApiResponse<AuthResponse>> createOrganisateur(
            @Valid @RequestBody CreateOrganisateurRequest request) {
        AuthResponse response = authService.createOrganisateur(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Compte organisateur créé", response));
    }

    @GetMapping("/organisateurs")
    @Operation(summary = "Lister tous les organisateurs")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<UserSummaryResponse>>> getOrganisateurs() {
        List<UserSummaryResponse> list = userRepository.findByRole(Role.ORGANISATEUR).stream()
                .map(this::toSummary)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/participants")
    @Operation(summary = "Lister tous les participants")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<UserSummaryResponse>>> getParticipants() {
        List<UserSummaryResponse> list = userRepository.findByRole(Role.PARTICIPANT).stream()
                .map(this::toSummary)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PatchMapping("/users/{id}/toggle-actif")
    @Operation(summary = "Activer / Désactiver un utilisateur")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> toggleActif(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setActif(!user.isActif());
        userRepository.save(user);
        String msg = user.isActif() ? "Compte activé" : "Compte désactivé";
        return ResponseEntity.ok(ApiResponse.success(msg, null));
    }

    private UserSummaryResponse toSummary(User u) {
        return UserSummaryResponse.builder()
                .id(u.getId())
                .prenom(u.getPrenom())
                .nom(u.getNom())
                .email(u.getEmail())
                .specialite(u.getSpecialite())
                .role(u.getRole().name())
                .build();
    }
}

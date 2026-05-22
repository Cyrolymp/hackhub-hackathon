package com.hackhub.controller;

import com.hackhub.dto.request.CreateEquipeRequest;
import com.hackhub.dto.request.JoinEquipeRequest;
import com.hackhub.dto.response.ApiResponse;
import com.hackhub.dto.response.EquipeResponse;
import com.hackhub.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired private EquipeService equipeService;

    /** POST /api/equipes — Créer une équipe */
    @PostMapping
    public ResponseEntity<ApiResponse<EquipeResponse>> create(
            @Valid @RequestBody CreateEquipeRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Équipe créée.", equipeService.createEquipe(req, auth.getName())));
    }

    /** POST /api/equipes/join — Rejoindre une équipe par code */
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<EquipeResponse>> join(
            @Valid @RequestBody JoinEquipeRequest req, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Vous avez rejoint l'équipe.", equipeService.joinEquipe(req, auth.getName())));
    }

    /** GET /api/equipes/me — Mon équipe */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<EquipeResponse>> getMyEquipe(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(equipeService.getMyEquipe(auth.getName())));
    }

    /** GET /api/equipes/{id} — Détail équipe */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EquipeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(equipeService.getEquipeById(id)));
    }

    /** DELETE /api/equipes/leave — Quitter l'équipe */
    @DeleteMapping("/leave")
    public ResponseEntity<ApiResponse<Void>> leave(Authentication auth) {
        equipeService.quitterEquipe(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Vous avez quitté l'équipe.", null));
    }
}

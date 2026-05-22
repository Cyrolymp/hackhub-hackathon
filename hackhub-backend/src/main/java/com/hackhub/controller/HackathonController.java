package com.hackhub.controller;

import com.hackhub.dto.request.CreateHackathonRequest;
import com.hackhub.dto.response.ApiResponse;
import com.hackhub.dto.response.HackathonResponse;
import com.hackhub.dto.response.InscriptionResponse;
import com.hackhub.service.HackathonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {

    @Autowired private HackathonService hackathonService;

    /** GET /api/hackathons — Liste publique */
    @GetMapping
    public ResponseEntity<ApiResponse<List<HackathonResponse>>> getAll(
            @RequestParam(required = false) String q) {
        List<HackathonResponse> list = (q != null && !q.isBlank())
                ? hackathonService.searchHackathons(q)
                : hackathonService.getAllHackathons();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    /** GET /api/hackathons/{id} — Détail public */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HackathonResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(hackathonService.getById(id)));
    }

    /** GET /api/hackathons/me — Mes hackathons (organisateur) */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ORGANISATEUR','ADMIN')")
    public ResponseEntity<ApiResponse<List<HackathonResponse>>> getMine(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(hackathonService.getMyHackathons(auth.getName())));
    }

    /** POST /api/hackathons — Créer un hackathon */
    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANISATEUR','ADMIN')")
    public ResponseEntity<ApiResponse<HackathonResponse>> create(
            @Valid @RequestBody CreateHackathonRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Hackathon créé.", hackathonService.create(req, auth.getName())));
    }

    /** PUT /api/hackathons/{id} — Modifier un hackathon */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANISATEUR','ADMIN')")
    public ResponseEntity<ApiResponse<HackathonResponse>> update(
            @PathVariable Long id, @Valid @RequestBody CreateHackathonRequest req, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Hackathon mis à jour.", hackathonService.update(id, req, auth.getName())));
    }

    /** DELETE /api/hackathons/{id} — Supprimer un hackathon */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANISATEUR','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication auth) {
        hackathonService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Hackathon supprimé.", null));
    }

    /** GET /api/hackathons/{id}/equipes — Équipes inscrites (organisateur) */
    @GetMapping("/{id}/equipes")
    @PreAuthorize("hasAnyRole('ORGANISATEUR','ADMIN')")
    public ResponseEntity<ApiResponse<List<InscriptionResponse>>> getEquipesInscrits(
            @PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(hackathonService.getEquipesInscritesByHackathon(id, auth.getName())));
    }

    /** POST /api/hackathons/{id}/inscrire — Inscrire mon équipe */
    @PostMapping("/{id}/inscrire")
    @PreAuthorize("hasRole('PARTICIPANT')")
    public ResponseEntity<ApiResponse<InscriptionResponse>> inscrire(
            @PathVariable Long id, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Équipe inscrite au hackathon.", hackathonService.inscrireEquipe(id, auth.getName())));
    }

    /** DELETE /api/hackathons/{id}/desinscrire — Désinscrire mon équipe */
    @DeleteMapping("/{id}/desinscrire")
    @PreAuthorize("hasRole('PARTICIPANT')")
    public ResponseEntity<ApiResponse<Void>> desinscrire(
            @PathVariable Long id, Authentication auth) {
        hackathonService.desinscrireEquipe(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Équipe désinscrite.", null));
    }
}

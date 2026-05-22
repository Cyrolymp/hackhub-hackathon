package com.hackhub.config;

import com.hackhub.dto.response.*;
import com.hackhub.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public UtilisateurResponse toUtilisateurResponse(Utilisateur u) {
        if (u == null) return null;
        return UtilisateurResponse.builder()
                .id(u.getId())
                .prenom(u.getPrenom())
                .nom(u.getNom())
                .email(u.getEmail())
                .specialite(u.getSpecialite())
                .bio(u.getBio())
                .role(u.getRole().name())
                .createdAt(u.getCreatedAt())
                .build();
    }

    public EquipeResponse toEquipeResponse(Equipe e) {
        if (e == null) return null;
        List<UtilisateurResponse> membres = e.getMembres() == null ? List.of() :
                e.getMembres().stream().map(this::toUtilisateurResponse).collect(Collectors.toList());
        return EquipeResponse.builder()
                .id(e.getId())
                .nom(e.getNom())
                .code(e.getCode())
                .specialite(e.getSpecialite())
                .description(e.getDescription())
                .tailleMax(e.getTailleMax())
                .nombreMembres(membres.size())
                .chef(toUtilisateurResponse(e.getChef()))
                .membres(membres)
                .createdAt(e.getCreatedAt())
                .build();
    }

    public HackathonResponse toHackathonResponse(Hackathon h) {
        if (h == null) return null;
        return HackathonResponse.builder()
                .id(h.getId())
                .titre(h.getTitre())
                .description(h.getDescription())
                .categorie(h.getCategorie())
                .lieu(h.getLieu())
                .dateDebut(h.getDateDebut())
                .dateFin(h.getDateFin())
                .placesMax(h.getPlacesMax())
                .nombreInscrits(h.getNombreInscrits())
                .placesRestantes(h.getPlacesRestantes())
                .prixRecompense(h.getPrixRecompense())
                .technologies(h.getTechnologies())
                .statut(h.getStatut().name())
                .organisateur(toUtilisateurResponse(h.getOrganisateur()))
                .createdAt(h.getCreatedAt())
                .build();
    }

    public InscriptionResponse toInscriptionResponse(Inscription i) {
        if (i == null) return null;
        return InscriptionResponse.builder()
                .id(i.getId())
                .equipe(toEquipeResponse(i.getEquipe()))
                .hackathon(toHackathonResponse(i.getHackathon()))
                .statut(i.getStatut().name())
                .dateInscription(i.getDateInscription())
                .build();
    }
}

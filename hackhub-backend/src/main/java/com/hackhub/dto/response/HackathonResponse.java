package com.hackhub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class HackathonResponse {
    private Long id;
    private String titre;
    private String description;
    private String categorie;
    private String lieu;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer placesMax;
    private int nombreInscrits;
    private int placesRestantes;
    private String prixRecompense;
    private List<String> technologies;
    private String statut;
    private UtilisateurResponse organisateur;
    private LocalDateTime createdAt;
}

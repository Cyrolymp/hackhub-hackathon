package com.hackhub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class EquipeResponse {
    private Long id;
    private String nom;
    private String code;
    private String specialite;
    private String description;
    private Integer tailleMax;
    private int nombreMembres;
    private UtilisateurResponse chef;
    private List<UtilisateurResponse> membres;
    private LocalDateTime createdAt;
}

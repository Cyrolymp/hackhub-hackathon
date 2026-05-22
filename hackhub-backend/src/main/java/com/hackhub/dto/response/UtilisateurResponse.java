package com.hackhub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class UtilisateurResponse {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String specialite;
    private String bio;
    private String role;
    private LocalDateTime createdAt;
}

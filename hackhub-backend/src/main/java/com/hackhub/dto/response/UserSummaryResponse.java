package com.hackhub.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserSummaryResponse {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String specialite;
    private String role;
}

package com.hackhub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterOrganisateurRequest {
    @NotBlank private String prenom;
    @NotBlank private String nom;
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 8) private String motDePasse;
    private String organisation;
}

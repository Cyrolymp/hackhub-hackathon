package com.hackhub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

// ---- Créer une équipe ----
@Data
public class EquipeRequest {

    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    private String nom;

    @NotNull(message = "L'ID du hackathon est obligatoire")
    private Long hackathonId;

    private String specialite;

    @Min(value = 1) @Max(value = 20)
    private Integer tailleMax = 4;

    private String description;

    // Code optionnel : si vide, il sera généré automatiquement
    private String code;
}

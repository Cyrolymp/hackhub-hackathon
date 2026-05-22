package com.hackhub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateEquipeRequest {
    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    private String nom;

    private String specialite;
    private String description;

    @Min(2) @Max(10)
    private Integer tailleMax = 4;
}

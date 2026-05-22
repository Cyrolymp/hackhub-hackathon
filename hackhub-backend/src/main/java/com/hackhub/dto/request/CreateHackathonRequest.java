package com.hackhub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateHackathonRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;

    private String lieu;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFin;

    @Min(1)
    private Integer placesMax = 100;

    private String prixRecompense;

    private List<String> technologies;
}

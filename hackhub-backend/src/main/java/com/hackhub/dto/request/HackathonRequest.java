package com.hackhub.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HackathonRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 20, message = "La description doit faire au moins 20 caractères")
    private String description;

    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "La date limite d'inscription est obligatoire")
    private LocalDate dateLimiteInscription;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 2, message = "Minimum 2 places")
    @Max(value = 10000, message = "Maximum 10 000 places")
    private Integer placesMax;

    @Min(value = 1, message = "Taille d'équipe minimum : 1")
    @Max(value = 20, message = "Taille d'équipe maximum : 20")
    private Integer tailleEquipeMax;

    @DecimalMin(value = "0.0", message = "Le prix ne peut pas être négatif")
    private BigDecimal prixTotal;

    private String descriptionPrix;

    private String technologies;

    // true = publier directement, false = sauvegarder comme brouillon
    private boolean publier = false;
}

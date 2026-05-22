package com.hackhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinEquipeRequest {
    @NotBlank(message = "Le code d'équipe est obligatoire")
    private String code;
}

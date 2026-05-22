package com.hackhub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class InscriptionResponse {
    private Long id;
    private EquipeResponse equipe;
    private HackathonResponse hackathon;
    private String statut;
    private LocalDateTime dateInscription;
}

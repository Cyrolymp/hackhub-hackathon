package com.hackhub.service;

import com.hackhub.dto.request.CreateEquipeRequest;
import com.hackhub.dto.request.JoinEquipeRequest;
import com.hackhub.dto.response.EquipeResponse;

public interface EquipeService {
    EquipeResponse createEquipe(CreateEquipeRequest request, String email);
    EquipeResponse joinEquipe(JoinEquipeRequest request, String email);
    EquipeResponse getMyEquipe(String email);
    EquipeResponse getEquipeById(Long id);
    void quitterEquipe(String email);
}

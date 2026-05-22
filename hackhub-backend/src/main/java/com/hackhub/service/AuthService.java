package com.hackhub.service;

import com.hackhub.dto.request.LoginRequest;
import com.hackhub.dto.request.RegisterOrganisateurRequest;
import com.hackhub.dto.request.RegisterRequest;
import com.hackhub.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerParticipant(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse registerOrganisateur(RegisterOrganisateurRequest request);
}

package com.hackhub.service.impl;

import com.hackhub.dto.request.LoginRequest;
import com.hackhub.dto.request.RegisterOrganisateurRequest;
import com.hackhub.dto.request.RegisterRequest;
import com.hackhub.dto.response.AuthResponse;
import com.hackhub.entity.Role;
import com.hackhub.entity.Utilisateur;
import com.hackhub.exception.HackHubException;
import com.hackhub.repository.UtilisateurRepository;
import com.hackhub.security.JwtUtils;
import com.hackhub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponse registerParticipant(RegisterRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new HackHubException("Cet email est déjà utilisé.", HttpStatus.CONFLICT);
        }
        Utilisateur user = Utilisateur.builder()
                .prenom(req.getPrenom())
                .nom(req.getNom())
                .email(req.getEmail())
                .motDePasse(passwordEncoder.encode(req.getMotDePasse()))
                .specialite(req.getSpecialite())
                .role(Role.PARTICIPANT)
                .build();
        utilisateurRepository.save(user);
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        Utilisateur user = utilisateurRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new HackHubException("Email ou mot de passe incorrect.", HttpStatus.UNAUTHORIZED));
        if (!user.isActif()) {
            throw new HackHubException("Ce compte est désactivé.", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(req.getMotDePasse(), user.getMotDePasse())) {
            throw new HackHubException("Email ou mot de passe incorrect.", HttpStatus.UNAUTHORIZED);
        }
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse registerOrganisateur(RegisterOrganisateurRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new HackHubException("Cet email est déjà utilisé.", HttpStatus.CONFLICT);
        }
        Utilisateur user = Utilisateur.builder()
                .prenom(req.getPrenom())
                .nom(req.getNom())
                .email(req.getEmail())
                .motDePasse(passwordEncoder.encode(req.getMotDePasse()))
                .specialite(req.getOrganisation())
                .role(Role.ORGANISATEUR)
                .build();
        utilisateurRepository.save(user);
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(Utilisateur user) {
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .email(user.getEmail())
                .role(user.getRole().name())
                .specialite(user.getSpecialite())
                .build();
    }
}

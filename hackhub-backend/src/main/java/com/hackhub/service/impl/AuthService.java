package com.hackhub.service.impl;

import com.hackhub.dto.request.*;
import com.hackhub.dto.response.AuthResponse;
import com.hackhub.entity.Role;
import com.hackhub.entity.User;
import com.hackhub.exception.BusinessException;
import com.hackhub.exception.ConflictException;
import com.hackhub.repository.UserRepository;
import com.hackhub.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    // ---- Inscription participant ----
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Un compte existe déjà avec cet email");
        }

        User user = User.builder()
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .specialite(request.getSpecialite())
                .role(Role.PARTICIPANT)
                .build();

        userRepository.save(user);
        log.info("Nouveau participant inscrit : {}", user.getEmail());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        return buildAuthResponse(token, user);
    }

    // ---- Connexion (tous rôles) ----
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable"));

        if (!user.isActif()) {
            throw new BusinessException("Votre compte a été désactivé. Contactez l'administrateur.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        log.info("Connexion réussie : {} [{}]", user.getEmail(), user.getRole());
        return buildAuthResponse(token, user);
    }

    // ---- Création organisateur (admin only) ----
    @Transactional
    public AuthResponse createOrganisateur(CreateOrganisateurRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Un compte existe déjà avec cet email");
        }

        User user = User.builder()
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .specialite(request.getOrganisation())
                .role(Role.ORGANISATEUR)
                .build();

        userRepository.save(user);
        log.info("Nouvel organisateur créé : {}", user.getEmail());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        return buildAuthResponse(token, user);
    }

    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .email(user.getEmail())
                .role(user.getRole().name())
                .specialite(user.getSpecialite())
                .build();
    }
}

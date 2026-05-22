package com.hackhub.service.impl;

import com.hackhub.dto.request.EquipeRequest;
import com.hackhub.dto.request.JoinEquipeRequest;
import com.hackhub.dto.response.EquipeResponse;
import com.hackhub.dto.response.UserSummaryResponse;
import com.hackhub.entity.*;
import com.hackhub.exception.BusinessException;
import com.hackhub.exception.ConflictException;
import com.hackhub.exception.ResourceNotFoundException;
import com.hackhub.repository.EquipeRepository;
import com.hackhub.repository.HackathonRepository;
import com.hackhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;

    // ---- Créer une équipe ----
    @Transactional
    public EquipeResponse createEquipe(EquipeRequest request, String chefEmail) {
        User chef = findUserOrThrow(chefEmail);
        Hackathon hackathon = hackathonRepository.findById(request.getHackathonId())
                .orElseThrow(() -> new ResourceNotFoundException("Hackathon", request.getHackathonId()));

        // Vérifications
        if (hackathon.getStatut() != HackathonStatut.OUVERT) {
            throw new BusinessException("Les inscriptions ne sont pas ouvertes pour ce hackathon");
        }
        if (chef.getEquipe() != null && hackathon.getId().equals(chef.getEquipe().getHackathon().getId())) {
            throw new BusinessException("Vous êtes déjà inscrit à ce hackathon dans une équipe");
        }

        // Générer/valider le code
        String code = (request.getCode() != null && !request.getCode().isBlank())
                ? request.getCode().toUpperCase()
                : generateUniqueCode();

        if (equipeRepository.existsByCode(code)) {
            throw new ConflictException("Ce code d'équipe est déjà utilisé. Veuillez en choisir un autre.");
        }

        Equipe equipe = Equipe.builder()
                .nom(request.getNom())
                .code(code)
                .specialite(request.getSpecialite())
                .tailleMax(request.getTailleMax())
                .description(request.getDescription())
                .chef(chef)
                .hackathon(hackathon)
                .build();

        equipe.getMembers().add(chef);
        equipeRepository.save(equipe);

        // Associer l'utilisateur à l'équipe
        chef.setEquipe(equipe);
        userRepository.save(chef);

        log.info("Équipe {} créée par {} pour le hackathon {}", code, chefEmail, hackathon.getTitre());
        return toResponse(equipe);
    }

    // ---- Rejoindre une équipe ----
    @Transactional
    public EquipeResponse joinEquipe(JoinEquipeRequest request, String userEmail) {
        User user = findUserOrThrow(userEmail);
        Equipe equipe = equipeRepository.findByCode(request.getCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Aucune équipe trouvée avec ce code : " + request.getCode()));

        // Vérifications
        if (equipe.getHackathon().getStatut() != HackathonStatut.OUVERT) {
            throw new BusinessException("Les inscriptions ne sont pas ouvertes pour ce hackathon");
        }
        if (user.getEquipe() != null && equipe.getHackathon().getId().equals(user.getEquipe().getHackathon().getId())) {
            throw new BusinessException("Vous êtes déjà inscrit à ce hackathon");
        }
        if (equipe.isComplet()) {
            throw new BusinessException("Cette équipe est complète");
        }
        if (equipe.getMembers().stream().anyMatch(m -> m.getId().equals(user.getId()))) {
            throw new BusinessException("Vous êtes déjà membre de cette équipe");
        }

        equipe.getMembers().add(user);
        user.setEquipe(equipe);

        equipeRepository.save(equipe);
        userRepository.save(user);

        log.info("{} a rejoint l'équipe {}", userEmail, equipe.getCode());
        return toResponse(equipe);
    }

    // ---- Quitter une équipe ----
    @Transactional
    public void quitterEquipe(Long equipeId, String userEmail) {
        User user = findUserOrThrow(userEmail);
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe", equipeId));

        if (equipe.getChef().getId().equals(user.getId())) {
            throw new BusinessException("Le chef d'équipe ne peut pas quitter l'équipe. Dissolvez-la ou transférez la direction.");
        }

        equipe.getMembers().removeIf(m -> m.getId().equals(user.getId()));
        user.setEquipe(null);
        equipeRepository.save(equipe);
        userRepository.save(user);
    }

    // ---- Equipes d'un hackathon ----
    @Transactional(readOnly = true)
    public List<EquipeResponse> getEquipesByHackathon(Long hackathonId) {
        return equipeRepository.findByHackathonId(hackathonId).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---- Mon équipe ----
    @Transactional(readOnly = true)
    public EquipeResponse getMyEquipe(String userEmail) {
        User user = findUserOrThrow(userEmail);
        if (user.getEquipe() == null) {
            throw new ResourceNotFoundException("Vous n'êtes dans aucune équipe");
        }
        return toResponse(user.getEquipe());
    }

    // ---- Helpers ----
    private User findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = "TEAM-" + UUID.randomUUID().toString()
                    .replace("-", "").substring(0, 4).toUpperCase();
        } while (equipeRepository.existsByCode(code));
        return code;
    }

    public EquipeResponse toResponse(Equipe e) {
        List<UserSummaryResponse> membres = e.getMembers().stream()
                .map(m -> UserSummaryResponse.builder()
                        .id(m.getId())
                        .prenom(m.getPrenom())
                        .nom(m.getNom())
                        .email(m.getEmail())
                        .specialite(m.getSpecialite())
                        .role(m.getRole().name())
                        .build())
                .toList();

        return EquipeResponse.builder()
                .id(e.getId())
                .nom(e.getNom())
                .code(e.getCode())
                .specialite(e.getSpecialite())
                .tailleMax(e.getTailleMax())
                .description(e.getDescription())
                .chefId(e.getChef().getId())
                .chefNom(e.getChef().getNomComplet())
                .hackathonId(e.getHackathon() != null ? e.getHackathon().getId() : null)
                .hackathonTitre(e.getHackathon() != null ? e.getHackathon().getTitre() : null)
                .membres(membres)
                .nombreMembres(e.getNombreMembers())
                .complet(e.isComplet())
                .createdAt(e.getCreatedAt())
                .build();
    }
}

package com.hackhub.service.impl;

import com.hackhub.config.Mapper;
import com.hackhub.dto.request.CreateEquipeRequest;
import com.hackhub.dto.request.JoinEquipeRequest;
import com.hackhub.dto.response.EquipeResponse;
import com.hackhub.entity.Equipe;
import com.hackhub.entity.Utilisateur;
import com.hackhub.exception.HackHubException;
import com.hackhub.repository.EquipeRepository;
import com.hackhub.repository.UtilisateurRepository;
import com.hackhub.service.EquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class EquipeServiceImpl implements EquipeService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired private EquipeRepository equipeRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private Mapper mapper;

    @Override
    @Transactional
    public EquipeResponse createEquipe(CreateEquipeRequest req, String email) {
        Utilisateur user = getUser(email);
        if (user.getEquipe() != null) {
            throw new HackHubException("Vous appartenez déjà à une équipe. Quittez-la d'abord.");
        }
        String code = generateUniqueCode();
        Equipe equipe = Equipe.builder()
                .nom(req.getNom())
                .code(code)
                .specialite(req.getSpecialite())
                .description(req.getDescription())
                .tailleMax(req.getTailleMax() != null ? req.getTailleMax() : 4)
                .chef(user)
                .build();
        equipe = equipeRepository.save(equipe);
        user.setEquipe(equipe);
        utilisateurRepository.save(user);
        return mapper.toEquipeResponse(equipe);
    }

    @Override
    @Transactional
    public EquipeResponse joinEquipe(JoinEquipeRequest req, String email) {
        Utilisateur user = getUser(email);
        if (user.getEquipe() != null) {
            throw new HackHubException("Vous appartenez déjà à une équipe.");
        }
        Equipe equipe = equipeRepository.findByCode(req.getCode().toUpperCase())
                .orElseThrow(() -> new HackHubException("Code d'équipe introuvable.", HttpStatus.NOT_FOUND));
        if (equipe.getMembres().size() >= equipe.getTailleMax()) {
            throw new HackHubException("Cette équipe est complète (" + equipe.getTailleMax() + " membres max).");
        }
        user.setEquipe(equipe);
        utilisateurRepository.save(user);
        return mapper.toEquipeResponse(equipeRepository.findById(equipe.getId()).orElseThrow());
    }

    @Override
    public EquipeResponse getMyEquipe(String email) {
        Utilisateur user = getUser(email);
        if (user.getEquipe() == null) {
            throw new HackHubException("Vous n'avez pas d'équipe.", HttpStatus.NOT_FOUND);
        }
        return mapper.toEquipeResponse(user.getEquipe());
    }

    @Override
    public EquipeResponse getEquipeById(Long id) {
        return mapper.toEquipeResponse(equipeRepository.findById(id)
                .orElseThrow(() -> new HackHubException("Équipe introuvable.", HttpStatus.NOT_FOUND)));
    }

    @Override
    @Transactional
    public void quitterEquipe(String email) {
        Utilisateur user = getUser(email);
        if (user.getEquipe() == null) {
            throw new HackHubException("Vous n'avez pas d'équipe.");
        }
        Equipe equipe = user.getEquipe();
        if (equipe.getChef().getId().equals(user.getId())) {
            throw new HackHubException("Le chef d'équipe ne peut pas quitter l'équipe sans la dissoudre.");
        }
        user.setEquipe(null);
        utilisateurRepository.save(user);
    }

    private Utilisateur getUser(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new HackHubException("Utilisateur introuvable.", HttpStatus.NOT_FOUND));
    }

    private String generateUniqueCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder("TEAM-");
            for (int i = 0; i < 4; i++) sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            code = sb.toString();
        } while (equipeRepository.existsByCode(code));
        return code;
    }
}

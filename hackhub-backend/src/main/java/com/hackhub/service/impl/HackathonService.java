package com.hackhub.service.impl;

import com.hackhub.dto.request.HackathonRequest;
import com.hackhub.dto.response.EquipeResponse;
import com.hackhub.dto.response.HackathonResponse;
import com.hackhub.entity.*;
import com.hackhub.exception.BusinessException;
import com.hackhub.exception.ResourceNotFoundException;
import com.hackhub.repository.HackathonRepository;
import com.hackhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final EquipeService equipeService;

    // ---- Lister tous les hackathons publics ----
    @Transactional(readOnly = true)
    public List<HackathonResponse> getAll() {
        return hackathonRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // ---- Lister uniquement les ouverts ----
    @Transactional(readOnly = true)
    public List<HackathonResponse> getAllOpen() {
        return hackathonRepository.findAllOpen().stream()
                .map(this::toResponse)
                .toList();
    }

    // ---- Recherche ----
    @Transactional(readOnly = true)
    public List<HackathonResponse> search(String keyword) {
        return hackathonRepository.searchByKeyword(keyword).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---- Détail d'un hackathon ----
    @Transactional(readOnly = true)
    public HackathonResponse getById(Long id) {
        Hackathon h = findOrThrow(id);
        return toResponse(h);
    }

    // ---- Hackathons de l'organisateur connecté ----
    @Transactional(readOnly = true)
    public List<HackathonResponse> getByOrganisateur(String email) {
        User org = findUserOrThrow(email);
        return hackathonRepository.findByOrganisateurId(org.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---- Créer un hackathon ----
    @Transactional
    public HackathonResponse create(HackathonRequest request, String organisateurEmail) {
        validateDates(request);
        User organisateur = findUserOrThrow(organisateurEmail);

        Hackathon hackathon = Hackathon.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .categorie(request.getCategorie())
                .lieu(request.getLieu())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .dateLimiteInscription(request.getDateLimiteInscription())
                .placesMax(request.getPlacesMax())
                .tailleEquipeMax(request.getTailleEquipeMax())
                .prixTotal(request.getPrixTotal())
                .descriptionPrix(request.getDescriptionPrix())
                .technologies(request.getTechnologies())
                .organisateur(organisateur)
                .statut(request.isPublier() ? HackathonStatut.OUVERT : HackathonStatut.BROUILLON)
                .build();

        hackathonRepository.save(hackathon);
        log.info("Hackathon créé : {} par {}", hackathon.getTitre(), organisateurEmail);
        return toResponse(hackathon);
    }

    // ---- Mettre à jour ----
    @Transactional
    public HackathonResponse update(Long id, HackathonRequest request, String organisateurEmail) {
        Hackathon hackathon = findOrThrow(id);
        checkOwnership(hackathon, organisateurEmail);
        validateDates(request);

        hackathon.setTitre(request.getTitre());
        hackathon.setDescription(request.getDescription());
        hackathon.setCategorie(request.getCategorie());
        hackathon.setLieu(request.getLieu());
        hackathon.setDateDebut(request.getDateDebut());
        hackathon.setDateFin(request.getDateFin());
        hackathon.setDateLimiteInscription(request.getDateLimiteInscription());
        hackathon.setPlacesMax(request.getPlacesMax());
        hackathon.setTailleEquipeMax(request.getTailleEquipeMax());
        hackathon.setPrixTotal(request.getPrixTotal());
        hackathon.setDescriptionPrix(request.getDescriptionPrix());
        hackathon.setTechnologies(request.getTechnologies());
        if (request.isPublier() && hackathon.getStatut() == HackathonStatut.BROUILLON) {
            hackathon.setStatut(HackathonStatut.OUVERT);
        }

        return toResponse(hackathonRepository.save(hackathon));
    }

    // ---- Changer le statut ----
    @Transactional
    public HackathonResponse changeStatut(Long id, String statutStr, String email) {
        Hackathon hackathon = findOrThrow(id);
        checkOwnership(hackathon, email);

        HackathonStatut newStatut;
        try {
            newStatut = HackathonStatut.valueOf(statutStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Statut invalide : " + statutStr);
        }

        hackathon.setStatut(newStatut);
        return toResponse(hackathonRepository.save(hackathon));
    }

    // ---- Supprimer ----
    @Transactional
    public void delete(Long id, String email) {
        Hackathon hackathon = findOrThrow(id);
        checkOwnership(hackathon, email);
        if (hackathon.getStatut() == HackathonStatut.EN_COURS) {
            throw new BusinessException("Impossible de supprimer un hackathon en cours");
        }
        hackathonRepository.delete(hackathon);
        log.info("Hackathon {} supprimé par {}", id, email);
    }

    // ---- Equipes d'un hackathon (organisateur) ----
    @Transactional(readOnly = true)
    public List<EquipeResponse> getEquipes(Long hackathonId, String email) {
        Hackathon hackathon = findOrThrow(hackathonId);
        checkOwnership(hackathon, email);
        return equipeService.getEquipesByHackathon(hackathonId);
    }

    // ---- Helpers ----
    private Hackathon findOrThrow(Long id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hackathon", id));
    }

    private User findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + email));
    }

    private void checkOwnership(Hackathon hackathon, String email) {
        if (!hackathon.getOrganisateur().getEmail().equals(email)) {
            throw new BusinessException("Vous n'êtes pas l'organisateur de ce hackathon");
        }
    }

    private void validateDates(HackathonRequest req) {
        if (req.getDateFin().isBefore(req.getDateDebut())) {
            throw new BusinessException("La date de fin doit être après la date de début");
        }
        if (req.getDateLimiteInscription().isAfter(req.getDateDebut())) {
            throw new BusinessException("La date limite d'inscription doit être avant le début du hackathon");
        }
    }

    public HackathonResponse toResponse(Hackathon h) {
        return HackathonResponse.builder()
                .id(h.getId())
                .titre(h.getTitre())
                .description(h.getDescription())
                .categorie(h.getCategorie())
                .lieu(h.getLieu())
                .dateDebut(h.getDateDebut())
                .dateFin(h.getDateFin())
                .dateLimiteInscription(h.getDateLimiteInscription())
                .placesMax(h.getPlacesMax())
                .tailleEquipeMax(h.getTailleEquipeMax())
                .prixTotal(h.getPrixTotal())
                .descriptionPrix(h.getDescriptionPrix())
                .technologies(h.getTechnologies())
                .statut(h.getStatut())
                .organisateurId(h.getOrganisateur().getId())
                .organisateurNom(h.getOrganisateur().getNomComplet())
                .nombreEquipes(h.getNombreEquipes())
                .nombreInscrits(h.getNombreInscrits())
                .complet(h.isComplet())
                .createdAt(h.getCreatedAt())
                .build();
    }
}

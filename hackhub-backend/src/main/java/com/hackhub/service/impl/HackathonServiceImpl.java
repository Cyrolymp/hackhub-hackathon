package com.hackhub.service.impl;

import com.hackhub.config.Mapper;
import com.hackhub.dto.request.CreateHackathonRequest;
import com.hackhub.dto.response.HackathonResponse;
import com.hackhub.dto.response.InscriptionResponse;
import com.hackhub.entity.*;
import com.hackhub.exception.HackHubException;
import com.hackhub.repository.*;
import com.hackhub.service.HackathonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HackathonServiceImpl implements HackathonService {

    @Autowired private HackathonRepository hackathonRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private InscriptionRepository inscriptionRepository;
    @Autowired private Mapper mapper;

    @Override
    public List<HackathonResponse> getAllHackathons() {
        return hackathonRepository.findByStatutNot(StatutHackathon.ANNULE)
                .stream().map(mapper::toHackathonResponse).collect(Collectors.toList());
    }

    @Override
    public List<HackathonResponse> searchHackathons(String query) {
        return hackathonRepository.search(query)
                .stream().map(mapper::toHackathonResponse).collect(Collectors.toList());
    }

    @Override
    public HackathonResponse getById(Long id) {
        return mapper.toHackathonResponse(findById(id));
    }

    @Override
    @Transactional
    public HackathonResponse create(CreateHackathonRequest req, String email) {
        Utilisateur organisateur = getUser(email);
        if (req.getDateFin().isBefore(req.getDateDebut())) {
            throw new HackHubException("La date de fin doit être après la date de début.");
        }
        Hackathon h = Hackathon.builder()
                .titre(req.getTitre())
                .description(req.getDescription())
                .categorie(req.getCategorie())
                .lieu(req.getLieu())
                .dateDebut(req.getDateDebut())
                .dateFin(req.getDateFin())
                .placesMax(req.getPlacesMax() != null ? req.getPlacesMax() : 100)
                .prixRecompense(req.getPrixRecompense())
                .technologies(req.getTechnologies() != null ? req.getTechnologies() : List.of())
                .statut(StatutHackathon.OUVERT)
                .organisateur(organisateur)
                .build();
        return mapper.toHackathonResponse(hackathonRepository.save(h));
    }

    @Override
    @Transactional
    public HackathonResponse update(Long id, CreateHackathonRequest req, String email) {
        Hackathon h = findById(id);
        checkOwnership(h, email);
        h.setTitre(req.getTitre());
        h.setDescription(req.getDescription());
        h.setCategorie(req.getCategorie());
        h.setLieu(req.getLieu());
        h.setDateDebut(req.getDateDebut());
        h.setDateFin(req.getDateFin());
        if (req.getPlacesMax() != null) h.setPlacesMax(req.getPlacesMax());
        if (req.getPrixRecompense() != null) h.setPrixRecompense(req.getPrixRecompense());
        if (req.getTechnologies() != null) h.setTechnologies(req.getTechnologies());
        return mapper.toHackathonResponse(hackathonRepository.save(h));
    }

    @Override
    @Transactional
    public void delete(Long id, String email) {
        Hackathon h = findById(id);
        checkOwnership(h, email);
        hackathonRepository.delete(h);
    }

    @Override
    public List<HackathonResponse> getMyHackathons(String email) {
        Utilisateur user = getUser(email);
        return hackathonRepository.findByOrganisateurId(user.getId())
                .stream().map(mapper::toHackathonResponse).collect(Collectors.toList());
    }

    @Override
    public List<InscriptionResponse> getEquipesInscritesByHackathon(Long hackathonId, String email) {
        Hackathon h = findById(hackathonId);
        checkOwnership(h, email);
        return inscriptionRepository.findByHackathonId(hackathonId)
                .stream().map(mapper::toInscriptionResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InscriptionResponse inscrireEquipe(Long hackathonId, String email) {
        Utilisateur user = getUser(email);
        Equipe equipe = user.getEquipe();
        if (equipe == null) {
            throw new HackHubException("Vous devez appartenir à une équipe pour vous inscrire.");
        }
        Hackathon h = findById(hackathonId);
        if (h.getStatut() != StatutHackathon.OUVERT) {
            throw new HackHubException("Les inscriptions sont fermées pour ce hackathon.");
        }
        if (h.getPlacesRestantes() <= 0) {
            throw new HackHubException("Ce hackathon est complet.");
        }
        if (inscriptionRepository.existsByEquipeIdAndHackathonId(equipe.getId(), hackathonId)) {
            throw new HackHubException("Votre équipe est déjà inscrite à ce hackathon.");
        }
        Inscription inscription = Inscription.builder()
                .equipe(equipe)
                .hackathon(h)
                .statut(StatutInscription.VALIDEE)
                .build();
        return mapper.toInscriptionResponse(inscriptionRepository.save(inscription));
    }

    @Override
    @Transactional
    public void desinscrireEquipe(Long hackathonId, String email) {
        Utilisateur user = getUser(email);
        if (user.getEquipe() == null) throw new HackHubException("Vous n'avez pas d'équipe.");
        Inscription inscription = inscriptionRepository
                .findByEquipeIdAndHackathonId(user.getEquipe().getId(), hackathonId)
                .orElseThrow(() -> new HackHubException("Inscription introuvable.", HttpStatus.NOT_FOUND));
        inscriptionRepository.delete(inscription);
    }

    private Hackathon findById(Long id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new HackHubException("Hackathon introuvable.", HttpStatus.NOT_FOUND));
    }

    private Utilisateur getUser(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new HackHubException("Utilisateur introuvable.", HttpStatus.NOT_FOUND));
    }

    private void checkOwnership(Hackathon h, String email) {
        Utilisateur user = getUser(email);
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = h.getOrganisateur().getId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            throw new HackHubException("Vous n'êtes pas autorisé à modifier ce hackathon.", HttpStatus.FORBIDDEN);
        }
    }
}

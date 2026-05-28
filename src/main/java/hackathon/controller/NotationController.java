package hackathon.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hackathon.domain.Compte;
import hackathon.domain.Juge;
import hackathon.repository.CompteRepository;
import hackathon.repository.EquipeRepository;
import hackathon.repository.JugeRepository;
import hackathon.repository.NoterRepository;
import hackathon.util.Alert;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( { "ADMIN", "JUGE" } )
@RequestMapping( "/notation" )
public class NotationController {

	private final NoterRepository	noterRepository;
	private final EquipeRepository	equipeRepository;
	private final JugeRepository	jugeRepository;
	private final CompteRepository	compteRepository;

	/**
	 * Retrouve le juge correspondant à l'utilisateur connecté.
	 * Renvoie null si l'utilisateur n'a pas de profil juge (cas d'un admin par exemple).
	 */
	private Juge currentJuge( Principal principal ) {
		if ( principal == null ) {
			return null;
		}
		Compte c = compteRepository.findByPseudo( principal.getName() );
		if ( c == null ) {
			c = compteRepository.findByEmail( principal.getName() );
		}
		if ( c == null ) {
			return null;
		}
		return jugeRepository.findByIdCompte( c.getIdCompte() );
	}

	// Page de notation : formulaire + tableau des notes
	@GetMapping
	public String index( Model model, Principal principal ) {
		Juge currentJuge = currentJuge( principal );
		model.addAttribute( "currentJuge", currentJuge );
		// Si l'utilisateur connecté n'est pas un juge (cas admin), on lui propose
		// le choix d'un juge ; sinon on n'expose pas la liste.
		if ( currentJuge == null ) {
			model.addAttribute( "juges", jugeRepository.findAll() );
		}
		model.addAttribute( "equipes", equipeRepository.findAll() );
		model.addAttribute( "notes", noterRepository.toutesLesNotes() );
		return "notation/index.html";
	}

	// Enregistre (insère ou met à jour) une note
	@PostMapping
	public String noter( Long idJuge, Long idEquipe, BigDecimal note, Principal principal,
			RedirectAttributes ra ) {

		// Le juge est imposé par l'identité connectée (sauf admin sans profil juge)
		Juge currentJuge = currentJuge( principal );
		Long jugeActif = ( currentJuge != null ) ? currentJuge.getIdJuge() : idJuge;

		if ( jugeActif == null || idEquipe == null || note == null ) {
			ra.addFlashAttribute( "alert",
					new Alert( Alert.Color.DANGER, "Juge, équipe et note sont obligatoires" ) );
			return "redirect:/notation";
		}
		if ( note.compareTo( BigDecimal.ZERO ) < 0 || note.compareTo( new BigDecimal( "20" ) ) > 0 ) {
			ra.addFlashAttribute( "alert",
					new Alert( Alert.Color.DANGER, "La note doit être comprise entre 0 et 20" ) );
			return "redirect:/notation";
		}
		noterRepository.upsert( idEquipe, jugeActif, note );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Note enregistrée avec succès" ) );
		return "redirect:/notation";
	}

}

package hackathon.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	// Page de notation : formulaire + tableau des notes
	@GetMapping
	public String index( Model model ) {
		model.addAttribute( "juges", jugeRepository.findAll() );
		model.addAttribute( "equipes", equipeRepository.findAll() );
		model.addAttribute( "notes", noterRepository.toutesLesNotes() );
		return "notation/index.html";
	}

	// Enregistre (insère ou met à jour) une note
	@PostMapping
	public String noter( Long idJuge, Long idEquipe, BigDecimal note, RedirectAttributes ra ) {
		if ( idJuge == null || idEquipe == null || note == null ) {
			ra.addFlashAttribute( "alert", new Alert( Alert.Color.DANGER, "Juge, équipe et note sont obligatoires" ) );
			return "redirect:/notation";
		}
		if ( note.compareTo( BigDecimal.ZERO ) < 0 || note.compareTo( new BigDecimal( "20" ) ) > 0 ) {
			ra.addFlashAttribute( "alert", new Alert( Alert.Color.DANGER, "La note doit être comprise entre 0 et 20" ) );
			return "redirect:/notation";
		}
		noterRepository.upsert( idEquipe, idJuge, note );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Note enregistrée avec succès" ) );
		return "redirect:/notation";
	}

}

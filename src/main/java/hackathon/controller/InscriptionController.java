package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hackathon.dto.InscriptionForm;
import hackathon.repository.EpreuveRepository;
import hackathon.repository.HackathonRepository;
import hackathon.service.InscriptionException;
import hackathon.service.InscriptionService;
import hackathon.util.Alert;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InscriptionController {

	private final InscriptionService	inscriptionService;
	private final HackathonRepository	hackathonRepository;
	private final EpreuveRepository		epreuveRepository;

	// Données nécessaires au formulaire (listes pour la création d'équipe)
	private void remplirModele( Model model ) {
		model.addAttribute( "hackathons", hackathonRepository.findAll() );
		model.addAttribute( "epreuves", epreuveRepository.findAll() );
	}

	// Affiche le formulaire
	@GetMapping( "/inscription" )
	public String form( Model model ) {
		if ( !model.containsAttribute( "form" ) ) {
			var f = new InscriptionForm();
			f.setRole( "PARTICIPANT" );
			f.setTeamMode( "JOIN" );
			model.addAttribute( "form", f );
		}
		remplirModele( model );
		return "public/inscription.html";
	}

	// Traite l'inscription
	@PostMapping( "/inscription" )
	public String register( @ModelAttribute( "form" ) InscriptionForm form, Model model, RedirectAttributes ra ) {
		try {
			String msg = inscriptionService.inscrire( form );
			ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, msg ) );
			return "redirect:/connexion";
		} catch ( InscriptionException e ) {
			model.addAttribute( "alert", new Alert( Alert.Color.DANGER, e.getMessage() ) );
			model.addAttribute( "form", form );
			remplirModele( model );
			return "public/inscription.html";
		}
	}

}

package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hackathon.repository.EquipeRepository;
import hackathon.repository.HackathonRepository;
import hackathon.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PublicController {

	// -------
	// Composants injectés
	// -------

	private final HackathonRepository	hackathonRepository;
	private final EquipeRepository		equipeRepository;
	private final ParticipantRepository	participantRepository;

	// -------
	// Endpoints
	// -------

	// home() : accueil avec statistiques réelles
	@GetMapping( "/" )
	public String home( Model model ) {
		model.addAttribute( "nbHackathons", hackathonRepository.count() );
		model.addAttribute( "nbEquipes", equipeRepository.count() );
		model.addAttribute( "nbParticipants", participantRepository.count() );
		return "public/accueil.html";
	}

	// hackathons() : liste publique des hackathons
	@GetMapping( "/hackathons" )
	public String hackathons( Model model ) {
		model.addAttribute( "hackathons", hackathonRepository.findAll() );
		return "public/hackathons.html";
	}

	// mentionsLegales()
	@GetMapping( "/mentions-legales" )
	public String mentionsLegales() {
		return "public/mentions-legales.html";
	}

	// quiSommesNous()
	@GetMapping( "/qui-sommes-nous" )
	public String quiSommesNous() {
		return "public/qui-sommes-nous.html";
	}

}

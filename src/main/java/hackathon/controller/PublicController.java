package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hackathon.repository.HackathonRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PublicController {

	// -------
	// Composants injectés
	// -------

	private final HackathonRepository hackathonRepository;

	// -------
	// Endpoints
	// -------

	// home()

	@GetMapping( "/" )
	public String home() {
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

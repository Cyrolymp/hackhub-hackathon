package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hackathon.repository.EpreuveRepository;
import hackathon.repository.EquipeRepository;
import hackathon.repository.HackathonRepository;
import hackathon.repository.JugeRepository;
import hackathon.repository.NoterRepository;
import hackathon.repository.PartenaireRepository;
import hackathon.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

	private final HackathonRepository	hackathonRepository;
	private final EquipeRepository		equipeRepository;
	private final ParticipantRepository	participantRepository;
	private final JugeRepository		jugeRepository;
	private final EpreuveRepository		epreuveRepository;
	private final PartenaireRepository	partenaireRepository;
	private final NoterRepository		noterRepository;

	@GetMapping( "/dashboard" )
	public String dashboard( Model model ) {
		model.addAttribute( "nbHackathons", hackathonRepository.count() );
		model.addAttribute( "nbEquipes", equipeRepository.count() );
		model.addAttribute( "nbParticipants", participantRepository.count() );
		model.addAttribute( "nbJuges", jugeRepository.count() );
		model.addAttribute( "nbEpreuves", epreuveRepository.count() );
		model.addAttribute( "nbPartenaires", partenaireRepository.count() );
		model.addAttribute( "classement", noterRepository.classement() );
		model.addAttribute( "hackathons", hackathonRepository.findAll() );
		return "dashboard/index.html";
	}

}

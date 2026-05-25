package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hackathon.repository.EpreuveRepository;
import hackathon.repository.EquipeRepository;
import hackathon.repository.HackathonRepository;
import hackathon.repository.NoterRepository;
import hackathon.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EquipeDetailController {

	private final EquipeRepository		equipeRepository;
	private final HackathonRepository	hackathonRepository;
	private final EpreuveRepository		epreuveRepository;
	private final ParticipantRepository	participantRepository;
	private final NoterRepository		noterRepository;

	@GetMapping( "/equipes/{id}" )
	public String detail( @PathVariable Long id, Model model ) {

		var opt = equipeRepository.findById( id );
		if ( opt.isEmpty() ) {
			return "redirect:/hackathons";
		}
		var equipe = opt.get();
		model.addAttribute( "equipe", equipe );

		if ( equipe.getIdHack() != null ) {
			hackathonRepository.findById( equipe.getIdHack() )
					.ifPresent( h -> model.addAttribute( "hackathon", h ) );
		}
		if ( equipe.getIdEpreuve() != null ) {
			epreuveRepository.findById( equipe.getIdEpreuve() )
					.ifPresent( e -> model.addAttribute( "epreuve", e ) );
		}

		model.addAttribute( "participants", participantRepository.findByIdEquipe( id ) );
		model.addAttribute( "notes", noterRepository.notesEquipe( id ) );
		model.addAttribute( "moyenne", noterRepository.moyenneEquipe( id ) );

		return "public/equipe-detail.html";
	}

}

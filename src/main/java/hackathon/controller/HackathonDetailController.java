package hackathon.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hackathon.repository.EpreuveRepository;
import hackathon.repository.EquipeRepository;
import hackathon.repository.HackathonRepository;
import hackathon.repository.LiaisonRepository;
import hackathon.repository.MembreStaffRepository;
import hackathon.repository.OrganisateurHackathonRepository;
import hackathon.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HackathonDetailController {

	private final HackathonRepository				hackathonRepository;
	private final OrganisateurHackathonRepository	organisateurRepository;
	private final EpreuveRepository					epreuveRepository;
	private final EquipeRepository					equipeRepository;
	private final PartenaireRepository				partenaireRepository;
	private final MembreStaffRepository				membreStaffRepository;
	private final LiaisonRepository					liaisonRepository;

	@GetMapping( "/hackathons/{id}" )
	public String detail( @PathVariable Long id, Model model ) {

		var opt = hackathonRepository.findById( id );
		if ( opt.isEmpty() ) {
			return "redirect:/hackathons";
		}
		var h = opt.get();
		model.addAttribute( "hackathon", h );

		// Organisateur
		if ( h.getIdOrganisateur() != null ) {
			organisateurRepository.findById( h.getIdOrganisateur() )
					.ifPresent( o -> model.addAttribute( "organisateur", o ) );
		}

		// Épreuves (table Contenir)
		List<Long> idsEpreuves = liaisonRepository.epreuvesDuHackathon( id );
		model.addAttribute( "epreuves",
				idsEpreuves.isEmpty() ? List.of() : epreuveRepository.findAllById( idsEpreuves ) );

		// Équipes inscrites
		model.addAttribute( "equipes", equipeRepository.findByIdHack( id ) );

		// Partenaires (table Sponsoriser)
		List<Long> idsPart = liaisonRepository.partenairesDuHackathon( id );
		model.addAttribute( "partenaires",
				idsPart.isEmpty() ? List.of() : partenaireRepository.findAllById( idsPart ) );

		// Staff (table Avoir)
		List<Long> idsStaff = liaisonRepository.staffDuHackathon( id );
		model.addAttribute( "staff",
				idsStaff.isEmpty() ? List.of() : membreStaffRepository.findAllById( idsStaff ) );

		return "public/hackathon-detail.html";
	}

}

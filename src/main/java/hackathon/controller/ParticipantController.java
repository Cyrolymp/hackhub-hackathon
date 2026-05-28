package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hackathon.domain.Participant;
import hackathon.repository.CompteRepository;
import hackathon.repository.EquipeRepository;
import hackathon.service.ParticipantService;
import hackathon.util.Alert;
import hackathon.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( { "ADMIN", "ORGANISATEUR" } )
@RequestMapping( "/participant" )
@SessionAttributes( "pagingParticipant" )
public class ParticipantController {

	private final ParticipantService	participantService;
	private final EquipeRepository		equipeRepository;
	private final CompteRepository		compteRepository;

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingParticipant" ) Paging paging ) {
		return paging;
	}

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {
		buildListContent( paging, model );
		return "participant/list.html";
	}

	@PostMapping( "/list/content" )
	public String buildListContent( Paging paging, Model model ) {
		var page = participantService.getPage( paging );
		model.addAttribute( "list", page.getContent() );
		return "participant/list.html :: #async_content";
	}

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {
		participantService.delete( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return buildListContent( paging, model );
	}

	@GetMapping( "/form" )
	public String edit( Long id, Model model ) {
		Participant item = ( id == null ) ? new Participant() : participantService.findById( id );
		return buildPageForm( item, model );
	}

	@PostMapping( "/form" )
	public String save( @Valid @ModelAttribute( "item" ) Participant item, BindingResult result,
			Model model, RedirectAttributes ra ) {
		if ( result.hasErrors() ) {
			return buildPageForm( item, model );
		}
		participantService.save( item );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/participant/list";
	}

	private String buildPageForm( Participant item, Model model ) {
		model.addAttribute( "item", item );
		model.addAttribute( "equipes", equipeRepository.findAll() );
		model.addAttribute( "comptes", compteRepository.findAll() );
		return "participant/form.html";
	}

}

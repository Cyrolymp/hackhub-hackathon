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

import hackathon.domain.Mentor;
import hackathon.repository.CompteRepository;
import hackathon.service.MentorService;
import hackathon.util.Alert;
import hackathon.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( "ADMIN" )
@RequestMapping( "/mentor" )
@SessionAttributes( "pagingMentor" )
public class MentorController {

	private final MentorService		mentorService;
	private final CompteRepository	compteRepository;

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingMentor" ) Paging paging ) {
		return paging;
	}

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {
		buildListContent( paging, model );
		return "mentor/list.html";
	}

	@PostMapping( "/list/content" )
	public String buildListContent( Paging paging, Model model ) {
		var page = mentorService.getPage( paging );
		model.addAttribute( "list", page.getContent() );
		return "mentor/list.html :: #async_content";
	}

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {
		mentorService.delete( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return buildListContent( paging, model );
	}

	@GetMapping( "/form" )
	public String edit( Long id, Model model ) {
		Mentor item = ( id == null ) ? new Mentor() : mentorService.findById( id );
		return buildPageForm( item, model );
	}

	@PostMapping( "/form" )
	public String save( @Valid @ModelAttribute( "item" ) Mentor item, BindingResult result,
			Model model, RedirectAttributes ra ) {
		if ( result.hasErrors() ) {
			return buildPageForm( item, model );
		}
		mentorService.save( item );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/mentor/list";
	}

	private String buildPageForm( Mentor item, Model model ) {
		model.addAttribute( "item", item );
		model.addAttribute( "comptes", compteRepository.findAll() );
		return "mentor/form.html";
	}

}

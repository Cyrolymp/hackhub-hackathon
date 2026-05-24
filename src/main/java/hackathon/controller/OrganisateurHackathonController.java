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

import hackathon.domain.OrganisateurHackathon;
import hackathon.service.OrganisateurHackathonService;
import hackathon.util.Alert;
import hackathon.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( "ADMIN" )
@RequestMapping( "/organisateur" )
@SessionAttributes( "pagingOrganisateurHackathon" )
public class OrganisateurHackathonController {

	private final OrganisateurHackathonService organisateurHackathonService;

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingOrganisateurHackathon" ) Paging paging ) {
		return paging;
	}

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {
		buildListContent( paging, model );
		return "organisateur/list.html";
	}

	@PostMapping( "/list/content" )
	public String buildListContent( Paging paging, Model model ) {
		var page = organisateurHackathonService.getPage( paging );
		model.addAttribute( "list", page.getContent() );
		return "organisateur/list.html :: #async_content";
	}

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {
		organisateurHackathonService.delete( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return buildListContent( paging, model );
	}

	@GetMapping( "/form" )
	public String edit( Long id, Model model ) {
		OrganisateurHackathon item = ( id == null ) ? new OrganisateurHackathon() : organisateurHackathonService.findById( id );
		return buildPageForm( item, model );
	}

	@PostMapping( "/form" )
	public String save( @ModelAttribute( "item" ) OrganisateurHackathon item, BindingResult result,
			Model model, RedirectAttributes ra ) {
		if ( result.hasErrors() ) {
			return buildPageForm( item, model );
		}
		organisateurHackathonService.save( item );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/organisateur/list";
	}

	private String buildPageForm( OrganisateurHackathon item, Model model ) {
		model.addAttribute( "item", item );
		return "organisateur/form.html";
	}

}

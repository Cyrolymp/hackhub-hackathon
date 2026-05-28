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

import hackathon.domain.Juge;
import hackathon.repository.CompteRepository;
import hackathon.service.JugeService;
import hackathon.util.Alert;
import hackathon.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( "ADMIN" )
@RequestMapping( "/juge" )
@SessionAttributes( "pagingJuge" )
public class JugeController {

	private final JugeService		jugeService;
	private final CompteRepository	compteRepository;

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingJuge" ) Paging paging ) {
		return paging;
	}

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {
		buildListContent( paging, model );
		return "juge/list.html";
	}

	@PostMapping( "/list/content" )
	public String buildListContent( Paging paging, Model model ) {
		var page = jugeService.getPage( paging );
		model.addAttribute( "list", page.getContent() );
		return "juge/list.html :: #async_content";
	}

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {
		jugeService.delete( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return buildListContent( paging, model );
	}

	@GetMapping( "/form" )
	public String edit( Long id, Model model ) {
		Juge item = ( id == null ) ? new Juge() : jugeService.findById( id );
		return buildPageForm( item, model );
	}

	@PostMapping( "/form" )
	public String save( @Valid @ModelAttribute( "item" ) Juge item, BindingResult result,
			Model model, RedirectAttributes ra ) {
		if ( result.hasErrors() ) {
			return buildPageForm( item, model );
		}
		jugeService.save( item );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/juge/list";
	}

	private String buildPageForm( Juge item, Model model ) {
		model.addAttribute( "item", item );
		model.addAttribute( "comptes", compteRepository.findAll() );
		return "juge/form.html";
	}

}

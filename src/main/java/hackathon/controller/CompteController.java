package hackathon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hackathon.domain.Compte;
import hackathon.repository.CompteRepository;
import hackathon.util.Alert;
import hackathon.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( "ADMIN" )
@RequestMapping( "/compte" )
@SessionAttributes( "pagingCompte" )
public class CompteController {

	// -------
	// Composants injectés
	// -------

	private final CompteRepository	compteRepository;
	private final PasswordEncoder	encoder;

	// -------
	// Attributs de session
	// -------

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingCompte" ) Paging paging ) {
		return paging;
	}

	// =======
	// Page Liste
	// =======

	// list()

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {

		buildListContent( paging, model );
		return "compte/list.html";

	}

	// delete()

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {

		compteRepository.deleteById( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return buildListContent( paging, model );

	}

	// buildListContent()
	// Contenu asynchrone de la page Listee

	@PostMapping( "/list/content" )
	public String buildListContent( Paging paging, Model model ) {
		var page = getPage( paging );
		model.addAttribute( "list", page.getContent() );
		return "compte/list.html :: #async_content";
	}

	// getPage() : pagination de la liste

	private Page<Compte> getPage( Paging paging ) {
		Page<Compte> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "pseudo" ) );
			if ( paging.getSearch() == null ) {
				page = compteRepository.findAll( pageable );
			} else {
				page = compteRepository.findByPseudoContainingIgnoreCaseOrEmailContainingIgnoreCase(
						paging.getSearch(), paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	// =======
	// Page Formulaire
	// =======

	// edit()

	@GetMapping( path = "/form" )
	public String edit( Long id, Model model ) {

		Compte item;
		if ( id == null ) {
			item = new Compte();
		} else {
			item = compteRepository.findById( id ).get();
		}
		return buildPageForm( item, model );

	}

	// save()

	@PostMapping( "/form" )
	public String save(
			@ModelAttribute( "item" ) Compte item,
			BindingResult result,
			Model model,
			RedirectAttributes ra ) {

		// Vérifie l'unicité du pseudo
		if ( !compteRepository.verifierUnicitePseudo( item.getPseudo(), item.getIdCompte() ) ) {
			result.rejectValue( "pseudo", "", "Ce pseudo est déjà utilisé" );
		}
		// Vérifie l'unicité de l'e-mail
		if ( !compteRepository.verifierUniciteEmail( item.getEmail(), item.getIdCompte() ) ) {
			result.rejectValue( "email", "", "Cet e-mail  est déjà utilisé" );
		}
		// Réaffiche la page Formulaire en cas d'erreurs
		if ( result.hasErrors() ) {
			return buildPageForm( item, model );
		}

		// Modifie le mot de passe si saisi
		if ( !item.getMotDePasse().isBlank() ) {
			item.setEmpreinteMdp( encoder.encode( item.getMotDePasse() ) );
		}

		// Effectue la mise à jour
		compteRepository.save( item );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/compte/list";

	}

	// buildPageForm()
	
	private String buildPageForm( Compte item, Model model ) {
		model.addAttribute( "item", item );
		return "compte/form.html";
	}

}

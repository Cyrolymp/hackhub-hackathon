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

import hackathon.domain.Epreuve;
import hackathon.domain.Equipe;
import hackathon.repository.EpreuveRepository;
import hackathon.repository.EquipeRepository;
import hackathon.repository.HackathonRepository;
import hackathon.service.EquipeService;
import hackathon.util.Alert;
import hackathon.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( { "ADMIN", "ORGANISATEUR" } )
@RequestMapping( "/equipe" )
@SessionAttributes( "pagingEquipe" )
public class EquipeController {

	private final EquipeService			equipeService;
	private final EquipeRepository		equipeRepository;
	private final HackathonRepository	hackathonRepository;
	private final EpreuveRepository		epreuveRepository;

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingEquipe" ) Paging paging ) {
		return paging;
	}

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {
		buildListContent( paging, model );
		return "equipe/list.html";
	}

	@PostMapping( "/list/content" )
	public String buildListContent( Paging paging, Model model ) {
		var page = equipeService.getPage( paging );
		model.addAttribute( "list", page.getContent() );
		return "equipe/list.html :: #async_content";
	}

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {
		equipeService.delete( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return buildListContent( paging, model );
	}

	@GetMapping( "/form" )
	public String edit( Long id, Model model ) {
		Equipe item = ( id == null ) ? new Equipe() : equipeService.findById( id );
		return buildPageForm( item, model );
	}

	@PostMapping( "/form" )
	public String save( @Valid @ModelAttribute( "item" ) Equipe item, BindingResult result,
			Model model, RedirectAttributes ra ) {

		// Contrôle de capacité : si on crée une nouvelle équipe sur cette épreuve,
		// on refuse si le nombre max d'équipes est atteint.
		if ( item.getIdEquipe() == null && item.getIdEpreuve() != null ) {
			Epreuve epreuve = epreuveRepository.findById( item.getIdEpreuve() ).orElse( null );
			if ( epreuve != null && epreuve.getNbrMaxEquipe() != null ) {
				long deja = equipeRepository.countByIdEpreuve( item.getIdEpreuve() );
				if ( deja >= epreuve.getNbrMaxEquipe() ) {
					result.rejectValue( "idEpreuve", "",
							"Cette épreuve a atteint sa capacité maximale ("
									+ epreuve.getNbrMaxEquipe() + " équipes)." );
				}
			}
		}

		if ( result.hasErrors() ) {
			return buildPageForm( item, model );
		}
		equipeService.save( item );
		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/equipe/list";
	}

	private String buildPageForm( Equipe item, Model model ) {
		model.addAttribute( "item", item );
		model.addAttribute( "hackathons", hackathonRepository.findAll() );
		model.addAttribute( "epreuves", epreuveRepository.findAll() );
		return "equipe/form.html";
	}

}

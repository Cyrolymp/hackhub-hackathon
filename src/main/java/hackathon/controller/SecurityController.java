package hackathon.controller;

import static hackathon.util.Alert.Color.DANGER;
import static hackathon.util.Alert.Color.SUCCESS;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hackathon.util.Alert;

@Controller
public class SecurityController {

	// -------
	// Endpoints
	// -------

	// connexion()

	@GetMapping( "/connexion" )
	public String connexion() {
		return "public/connexion.html";
	}

	// login()

	@GetMapping( "/login" )
	public String login( RedirectAttributes ra ) {
		ra.addFlashAttribute( "alert", new Alert( DANGER, "Vous n'êtes pas connecté·e" ) );
		return "redirect:/connexion";
	}

	// loginError()

	@GetMapping( path = "/login", params = "error" )
	public String loginError( RedirectAttributes ra ) {
		ra.addFlashAttribute( "alert", new Alert( DANGER, "Identifiant ou mot de passe erroné" ) );
		return "redirect:/connexion";
	}

	// accessDenied()

	@GetMapping( "/accessDenied" )
	public String accessDenied( RedirectAttributes ra ) {
		ra.addFlashAttribute( "alert", new Alert( DANGER, "Accès non autorié !" ) );
		return "redirect:/connexion";
	}

	// disconnected()

	@GetMapping( "/disconnected" )
	public String disconnected( RedirectAttributes ra ) {
		ra.addFlashAttribute( "alert", new Alert( SUCCESS, "Déconnexion effectuée avec succès !" ) );
		return "redirect:/connexion";
	}

}

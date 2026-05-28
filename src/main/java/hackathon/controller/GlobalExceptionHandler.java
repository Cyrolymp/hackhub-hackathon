package hackathon.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Filet de sécurité : transforme certaines exceptions en page d'erreur lisible
 * plutôt qu'en page blanche "Whitelabel".
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	// Violation de contrainte en base (clé étrangère inexistante, doublon, NOT NULL…)
	@ExceptionHandler( DataIntegrityViolationException.class )
	public String handleDataIntegrity( Model model, HttpServletResponse response ) {
		response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		model.addAttribute( "status", "Opération impossible" );
		model.addAttribute( "messageErreur",
				"L'opération n'a pas pu être réalisée : une donnée liée est invalide "
						+ "(référence inexistante, doublon, ou champ obligatoire manquant)." );
		return "error/error.html";
	}

}

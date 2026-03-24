package hackathon.controller;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PublicController {

	// -------
	// Champs
	// -------

	private final UserDetailsService	userDetailsService;
	private final RememberMeServices	rememberMeServices;

	private boolean autoConnect = true; // true -> active l'autoconnexion

	// -------
	// Endpoints
	// -------

	// -------
	// home()

	@GetMapping( "/" )
	public String home( HttpServletRequest request, HttpServletResponse response ) {

		// Connexion automatique avec le compe admin
		var	session	= request.getSession( true );
		var	sc		= (SecurityContext) session.getAttribute( SPRING_SECURITY_CONTEXT_KEY );
		if ( sc == null && autoConnect ) {
			var	userDetails	= userDetailsService.loadUserByUsername( "admin" );
			var	authReq		= new UsernamePasswordAuthenticationToken( userDetails, null,
					userDetails.getAuthorities() );
			rememberMeServices.loginSuccess( request, response, authReq );
			sc = SecurityContextHolder.getContext();
			sc.setAuthentication( authReq );
			session.setAttribute( SPRING_SECURITY_CONTEXT_KEY, sc );
		}

		autoConnect = false;
		return "public/accueil.html";
	}

	// -------
	// mentionsLegales()

	@GetMapping( "/mentions-legales" )
	public String mentionsLegales() {
		return "public/mentions-legales.html";
	}

	// -------
	// quiSommesNous()

	@GetMapping( "/qui-sommes-nous" )
	public String quiSommesNous() {
		return "public/qui-sommes-nous.html";
	}

}

package hackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hackathon.repository.NoterRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClassementController {

	private final NoterRepository noterRepository;

	// Page publique : classement des équipes par moyenne
	@GetMapping( "/classement" )
	public String classement( Model model ) {
		model.addAttribute( "classement", noterRepository.classement() );
		return "public/classement.html";
	}

}

package hackathon.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hackathon.domain.Compte;
import hackathon.repository.CompteRepository;
import hackathon.repository.JugeRepository;
import hackathon.repository.MentorRepository;
import hackathon.repository.OrganisateurHackathonRepository;
import hackathon.repository.ParticipantRepository;
import hackathon.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

	// -------
	// Composants injectés
	// -------

	private final CompteRepository					compteRepository;
	private final JugeRepository					jugeRepository;
	private final MentorRepository					mentorRepository;
	private final OrganisateurHackathonRepository	organisateurRepository;
	private final ParticipantRepository				participantRepository;
	private final PartenaireRepository				partenaireRepository;

	// -------
	// UserDetailsService
	// -------

	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {

		// Recherche le compte par pseudo, puis par e-mail
		Compte compte = compteRepository.findByPseudo( username );
		if ( compte == null ) {
			compte = compteRepository.findByEmail( username );
			if ( compte == null ) {
				throw new UsernameNotFoundException( "Le compte '" + username + "' n'existe pas" );
			}
		}

		// Déduit les rôles : ADMIN via la colonne role_admin,
		// les autres via le lien id_compte des entités métier
		Long			id		= compte.getIdCompte();
		List<String>	roles	= new ArrayList<>();
		roles.add( "USER" );
		if ( compte.isRoleAdmin() ) {
			roles.add( "ADMIN" );
		}
		if ( participantRepository.existsByIdCompte( id ) ) {
			roles.add( "PARTICIPANT" );
		}
		if ( jugeRepository.existsByIdCompte( id ) ) {
			roles.add( "JUGE" );
		}
		if ( organisateurRepository.existsByIdCompte( id ) ) {
			roles.add( "ORGANISATEUR" );
		}
		if ( partenaireRepository.existsByIdCompte( id ) ) {
			roles.add( "PARTENAIRE" );
		}
		if ( mentorRepository.existsByIdCompte( id ) ) {
			roles.add( "MENTOR" );
		}

		return User.withUsername( compte.getPseudo() )
				.password( compte.getEmpreinteMdp() )
				.roles( roles.toArray( new String[] {} ) )
				.build();
	}

}

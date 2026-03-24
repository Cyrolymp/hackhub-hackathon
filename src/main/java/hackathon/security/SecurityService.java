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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

	private final CompteRepository compteRepository;

	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {

		Compte compte = compteRepository.findByPseudo( username );
		if ( compte == null ) {
			compte = compteRepository.findByEmail( username );
			if ( compte == null ) {
				throw new UsernameNotFoundException( "Le compte '" + username + "' n'existe pas" );
			}
		}

		List<String> roles = new ArrayList<>();
		roles.add( "USER" );
		if ( compte.isRoleAdmin() ) {
			roles.add( "ADMIN" );
		}

		UserDetails userDetails = User.withUsername( compte.getPseudo() ).password( compte.getEmpreinteMdp() )
				.roles( roles.toArray( new String[] {} ) ).build();

		return userDetails;
	}
}

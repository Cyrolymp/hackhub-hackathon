package hackathon.service;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hackathon.domain.Compte;
import hackathon.domain.Equipe;
import hackathon.domain.Juge;
import hackathon.domain.Mentor;
import hackathon.domain.OrganisateurHackathon;
import hackathon.domain.Partenaire;
import hackathon.domain.Participant;
import hackathon.dto.InscriptionForm;
import hackathon.repository.CompteRepository;
import hackathon.repository.EpreuveRepository;
import hackathon.repository.EquipeRepository;
import hackathon.repository.JugeRepository;
import hackathon.repository.MentorRepository;
import hackathon.repository.OrganisateurHackathonRepository;
import hackathon.repository.ParticipantRepository;
import hackathon.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InscriptionService {

	private final CompteRepository					compteRepository;
	private final ParticipantRepository				participantRepository;
	private final EquipeRepository					equipeRepository;
	private final EpreuveRepository					epreuveRepository;
	private final JugeRepository					jugeRepository;
	private final OrganisateurHackathonRepository	organisateurRepository;
	private final PartenaireRepository				partenaireRepository;
	private final MentorRepository					mentorRepository;
	private final PasswordEncoder					encoder;

	/**
	 * Inscrit un nouvel utilisateur selon le rôle choisi.
	 * @return un message de succès (contient le code d'équipe si une équipe est créée)
	 */
	@Transactional
	public String inscrire( InscriptionForm f ) {

		// 1) Validation du compte
		if ( blank( f.getPseudo() ) ) {
			throw new InscriptionException( "Le pseudo est obligatoire" );
		}
		if ( !compteRepository.verifierUnicitePseudo( f.getPseudo(), null ) ) {
			throw new InscriptionException( "Ce pseudo est déjà utilisé" );
		}
		if ( blank( f.getEmail() ) ) {
			throw new InscriptionException( "L'e-mail est obligatoire" );
		}
		if ( !compteRepository.verifierUniciteEmail( f.getEmail(), null ) ) {
			throw new InscriptionException( "Cet e-mail est déjà utilisé" );
		}

		// Politique de mot de passe : au moins 8 caractères, une lettre et un chiffre
		String mdp = f.getMotDePasse();
		if ( blank( mdp ) ) {
			throw new InscriptionException( "Le mot de passe est obligatoire" );
		}
		if ( mdp.length() < 8 ) {
			throw new InscriptionException( "Le mot de passe doit faire au moins 8 caractères" );
		}
		if ( !mdp.matches( ".*[A-Za-zÀ-ÿ].*" ) || !mdp.matches( ".*[0-9].*" ) ) {
			throw new InscriptionException( "Le mot de passe doit contenir au moins une lettre et un chiffre" );
		}

		if ( blank( f.getRole() ) ) {
			throw new InscriptionException( "Choisissez un type de compte" );
		}

		// 2) Création du compte (mot de passe haché, rôle non-admin)
		Compte c = new Compte();
		c.setPseudo( f.getPseudo() );
		c.setEmail( f.getEmail() );
		c.setRoleAdmin( false );
		c.setEmpreinteMdp( encoder.encode( mdp ) );
		c = compteRepository.save( c );
		Long idCompte = c.getIdCompte();

		String msg = "Compte créé avec succès ! Vous pouvez vous connecter.";

		// 3) Création de l'entité métier selon le rôle
		switch ( f.getRole() ) {

			case "JUGE": {
				Juge j = new Juge();
				j.setNomJuge( req( f.getNom(), "votre nom" ) );
				j.setAdresseMailJuge( f.getEmail() );
				j.setSpecialite( orDefault( f.getSpecialite(), "Généraliste" ) );
				j.setIdCompte( idCompte );
				jugeRepository.save( j );
				break;
			}

			case "ORGANISATEUR": {
				OrganisateurHackathon o = new OrganisateurHackathon();
				o.setNomOrg( req( f.getNom(), "le nom de la structure" ) );
				o.setAdMailOrg( f.getEmail() );
				o.setIdCompte( idCompte );
				organisateurRepository.save( o );
				break;
			}

			case "PARTENAIRE": {
				Partenaire p = new Partenaire();
				p.setNomPartenaire( req( f.getNom(), "le nom du partenaire" ) );
				p.setContactPartenaire( orDefault( f.getContact(), f.getEmail() ) );
				p.setIdCompte( idCompte );
				partenaireRepository.save( p );
				break;
			}

			case "MENTOR": {
				Mentor m = new Mentor();
				m.setNomMentor( req( f.getNom(), "votre nom" ) );
				m.setAdresseMailMentor( f.getEmail() );
				m.setSpecialite( orDefault( f.getSpecialite(), "Généraliste" ) );
				m.setIdCompte( idCompte );
				mentorRepository.save( m );
				break;
			}

			case "PARTICIPANT": {
				Long idEquipe;
				if ( "CREATE".equals( f.getTeamMode() ) ) {
					Long idHack = parseLong( f.getIdHack() );
					Long idEpreuve = parseLong( f.getIdEpreuve() );
					if ( blank( f.getNomEquipe() ) ) {
						throw new InscriptionException( "Le nom de l'équipe est obligatoire" );
					}
					if ( idHack == null ) {
						throw new InscriptionException( "Choisissez un hackathon pour votre équipe" );
					}
					if ( idEpreuve == null ) {
						throw new InscriptionException( "Choisissez une épreuve pour votre équipe" );
					}
					// Contrôle de capacité de l'épreuve
					var epreuve = epreuveRepository.findById( idEpreuve ).orElse( null );
					if ( epreuve != null && epreuve.getNbrMaxEquipe() != null
							&& equipeRepository.countByIdEpreuve( idEpreuve ) >= epreuve.getNbrMaxEquipe() ) {
						throw new InscriptionException( "Cette épreuve a atteint sa capacité maximale ("
								+ epreuve.getNbrMaxEquipe() + " équipes)" );
					}
					Equipe eq = new Equipe();
					eq.setNomEquipe( f.getNomEquipe() );
					eq.setNomChef( orDefault( f.getNom(), f.getPseudo() ) );
					String code = genererCode();
					eq.setCodeEquipe( code );
					eq.setIdHack( idHack );
					eq.setIdEpreuve( idEpreuve );
					eq = equipeRepository.save( eq );
					idEquipe = eq.getIdEquipe();
					msg = "Équipe « " + f.getNomEquipe() + " » créée ! Code d'équipe : " + code
							+ " — partagez-le pour que vos coéquipiers vous rejoignent. Vous pouvez vous connecter.";
				} else {
					// JOIN
					if ( blank( f.getCodeEquipe() ) ) {
						throw new InscriptionException( "Saisissez le code de l'équipe à rejoindre" );
					}
					Equipe eq = equipeRepository.findByCodeEquipe( f.getCodeEquipe().trim().toUpperCase() );
					if ( eq == null ) {
						throw new InscriptionException( "Aucune équipe ne correspond à ce code" );
					}
					idEquipe = eq.getIdEquipe();
				}
				Participant part = new Participant();
				part.setNomParticipant( orDefault( f.getNom(), f.getPseudo() ) );
				part.setEtablissementParticipant( f.getEtablissement() );
				part.setMailParticipant( f.getEmail() );
				part.setIdEquipe( idEquipe );
				part.setIdCompte( idCompte );
				participantRepository.save( part );
				break;
			}

			default:
				throw new InscriptionException( "Type de compte inconnu" );
		}

		return msg;
	}

	// -------
	// Helpers
	// -------

	private boolean blank( String s ) {
		return s == null || s.isBlank();
	}

	private String req( String s, String quoi ) {
		if ( blank( s ) ) {
			throw new InscriptionException( "Veuillez renseigner " + quoi );
		}
		return s.trim();
	}

	private Long parseLong( String s ) {
		if ( blank( s ) ) {
			return null;
		}
		try {
			return Long.valueOf( s.trim() );
		} catch ( NumberFormatException e ) {
			return null;
		}
	}

	private String orDefault( String s, String def ) {
		return blank( s ) ? def : s.trim();
	}

	// Génère un code d'équipe unique de 6 caractères
	private String genererCode() {
		String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
		Random r = new Random();
		for ( int essai = 0; essai < 25; essai++ ) {
			StringBuilder sb = new StringBuilder();
			for ( int i = 0; i < 6; i++ ) {
				sb.append( chars.charAt( r.nextInt( chars.length() ) ) );
			}
			String code = sb.toString();
			if ( equipeRepository.findByCodeEquipe( code ) == null ) {
				return code;
			}
		}
		return "EQ" + ( System.currentTimeMillis() % 100000 );
	}

}

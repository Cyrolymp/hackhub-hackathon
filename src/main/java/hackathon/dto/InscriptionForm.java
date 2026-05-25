package hackathon.dto;

import lombok.Data;

@Data
public class InscriptionForm {

	// Compte (commun)
	private String	pseudo;
	private String	email;
	private String	motDePasse;
	private String	role;			// PARTICIPANT, JUGE, ORGANISATEUR, PARTENAIRE, MENTOR

	// Champ commun
	private String	nom;			// nom de la personne ou de la structure

	// Participant
	private String	etablissement;
	private String	teamMode;		// CREATE ou JOIN
	private String	nomEquipe;		// si CREATE
	private String	idHack;			// si CREATE
	private String	idEpreuve;		// si CREATE
	private String	codeEquipe;		// si JOIN

	// Juge / Mentor
	private String	specialite;

	// Partenaire
	private String	contact;

}

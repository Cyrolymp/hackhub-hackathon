package hackathon.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = { "idHack" } )
@Table( "hackathon" )
public class Hackathon {

	@Id
	private Long		idHack;

	@NotBlank( message = "Le nom est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String		nomHack;

	@NotBlank( message = "Le thème est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String		themeHach;

	@NotNull( message = "L'heure de début est obligatoire" )
	private LocalTime	heureDeb;

	private LocalDate	dateDeb;
	private LocalDate	dateFin;

	@NotBlank( message = "Le lieu est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String		lieuHack;

	@Size( max = 50, message = "50 caractères maximum" )
	private String		contact;

	@NotNull( message = "L'organisateur est obligatoire" )
	private Long		idOrganisateur;

}

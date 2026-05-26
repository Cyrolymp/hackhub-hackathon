package hackathon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
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
@EqualsAndHashCode( of = { "idParticipant" } )
@Table( "participant" )
public class Participant {

	@Id
	private Long	idParticipant;

	@Size( max = 50, message = "50 caractères maximum" )
	private String	nomParticipant;

	@Size( max = 50, message = "50 caractères maximum" )
	private String	etablissementParticipant;

	@NotBlank( message = "L'e-mail est obligatoire" )
	@Email( message = "Format d'e-mail invalide" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	mailParticipant;

	@NotNull( message = "L'équipe est obligatoire" )
	private Long	idEquipe;

	private Long	idCompte;

}

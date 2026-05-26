package hackathon.domain;

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
@EqualsAndHashCode( of = { "idEquipe" } )
@Table( "equipe" )
public class Equipe {

	@Id
	private Long	idEquipe;

	@NotBlank( message = "Le nom est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	nomEquipe;

	@NotBlank( message = "Le chef d'équipe est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	nomChef;

	@Size( max = 50, message = "50 caractères maximum" )
	private String	codeEquipe;

	@NotNull( message = "Le hackathon est obligatoire" )
	private Long	idHack;

	@NotNull( message = "L'épreuve est obligatoire" )
	private Long	idEpreuve;

}

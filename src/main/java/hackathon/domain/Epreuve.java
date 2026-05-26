package hackathon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Min;
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
@EqualsAndHashCode( of = { "idEpreuve" } )
@Table( "epreuve" )
public class Epreuve {

	@Id
	private Long	idEpreuve;

	@NotBlank( message = "Le nom est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	nomEpreuve;

	@NotNull( message = "Le nombre max d'équipes est obligatoire" )
	@Min( value = 1, message = "Au moins 1 équipe" )
	private Integer	nbrMaxEquipe;

	private Long	idRecompense;

}

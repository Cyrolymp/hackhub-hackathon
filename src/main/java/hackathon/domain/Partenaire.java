package hackathon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = { "idPartenaire" } )
@Table( "partenaire" )
public class Partenaire {

	@Id
	private Long	idPartenaire;

	@NotBlank( message = "Le nom est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	nomPartenaire;

	@NotBlank( message = "Le contact est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	contactPartenaire;

	private Long	idCompte;

}

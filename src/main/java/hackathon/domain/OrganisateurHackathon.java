package hackathon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = { "idOrganisateur" } )
@Table( "organisateurhackathon" )
public class OrganisateurHackathon {

	@Id
	private Long	idOrganisateur;

	@NotBlank( message = "Le nom est obligatoire" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	nomOrg;

	@NotBlank( message = "L'e-mail est obligatoire" )
	@Email( message = "Format d'e-mail invalide" )
	@Size( max = 50, message = "50 caractères maximum" )
	private String	adMailOrg;

	private Long	idCompte;

}

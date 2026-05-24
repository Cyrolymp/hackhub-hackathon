package hackathon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
	private String	nomOrg;
	private String	adMailOrg;
	private Long	idCompte;

}

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
@EqualsAndHashCode( of = { "idPartenaire" } )
@Table( "partenaire" )
public class Partenaire {

	@Id
	private Long	idPartenaire;
	private String	nomPartenaire;
	private String	contactPartenaire;
	private Long	idCompte;

}

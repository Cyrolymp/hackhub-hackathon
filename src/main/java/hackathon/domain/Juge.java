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
@EqualsAndHashCode( of = { "idJuge" } )
@Table( "juge" )
public class Juge {

	@Id
	private Long	idJuge;
	private String	nomJuge;
	private String	adresseMailJuge;
	private String	specialite;
	private Long	idCompte;

}

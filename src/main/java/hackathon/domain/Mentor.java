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
@EqualsAndHashCode( of = { "idMentor" } )
@Table( "mentor" )
public class Mentor {

	@Id
	private Long	idMentor;
	private String	nomMentor;
	private String	adresseMailMentor;
	private String	specialite;
	private Long	idCompte;

}

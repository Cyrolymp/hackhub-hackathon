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
@EqualsAndHashCode( of = { "idEquipe" } )
@Table( "equipe" )
public class Equipe {

	@Id
	private Long	idEquipe;
	private String	nomEquipe;
	private String	nomChef;
	private String	codeEquipe;
	private Long	idHack;
	private Long	idEpreuve;

}

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
@EqualsAndHashCode( of = { "idEpreuve" } )
@Table( "epreuve" )
public class Epreuve {

	@Id
	private Long	idEpreuve;
	private String	nomEpreuve;
	private Integer	nbrMaxEquipe;
	private Long	idRecompense;

}

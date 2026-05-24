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
@EqualsAndHashCode( of = { "idRecompense" } )
@Table( "recompense" )
public class Recompense {

	@Id
	private Long	idRecompense;
	private String	nomRecompense;
	private String	typeRecompense;

}

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
@EqualsAndHashCode( of = { "idParticipant" } )
@Table( "participant" )
public class Participant {

	@Id
	private Long	idParticipant;
	private String	nomParticipant;
	private String	etablissementParticipant;
	private String	mailParticipant;
	private Long	idEquipe;
	private Long	idCompte;

}

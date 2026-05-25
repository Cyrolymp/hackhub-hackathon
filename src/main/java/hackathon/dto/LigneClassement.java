package hackathon.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneClassement {

	private Long		idEquipe;
	private String		nomEquipe;
	private String		nomHack;
	private BigDecimal	moyenne;	// null si aucune note
	private int			nbNotes;

}

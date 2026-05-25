package hackathon.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneNote {

	private String		nomEquipe;
	private String		nomJuge;
	private BigDecimal	note;

}

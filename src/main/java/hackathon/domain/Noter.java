package hackathon.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Noter {

	private Long		idEquipe;
	private Long		idJuge;
	private BigDecimal	note;

}

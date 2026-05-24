package hackathon.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = { "idHack" } )
@Table( "hackathon" )
public class Hackathon {

	@Id
	private Long		idHack;
	private String		nomHack;
	private String		themeHach;
	private LocalTime	heureDeb;
	private LocalDate	dateDeb;
	private LocalDate	dateFin;
	private String		lieuHack;
	private String		contact;
	private Long		idOrganisateur;

}

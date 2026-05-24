package hackathon.repository;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

/**
 * Gère les tables d'association à clé composite :
 *  - Avoir       (hackathon <-> membre staff)
 *  - Contenir    (hackathon <-> epreuve)
 *  - Sponsoriser (hackathon <-> partenaire)
 */
@Repository
@RequiredArgsConstructor
public class LiaisonRepository {

	private final NamedParameterJdbcTemplate jdbc;

	private void lier( String table, String colA, Long valA, String colB, Long valB ) {
		var sql = "INSERT INTO " + table + " (" + colA + ", " + colB + ") "
				+ "VALUES (:a, :b) ON CONFLICT DO NOTHING";
		jdbc.update( sql, new MapSqlParameterSource().addValue( "a", valA ).addValue( "b", valB ) );
	}

	private void delier( String table, String colA, Long valA, String colB, Long valB ) {
		var sql = "DELETE FROM " + table + " WHERE " + colA + " = :a AND " + colB + " = :b";
		jdbc.update( sql, new MapSqlParameterSource().addValue( "a", valA ).addValue( "b", valB ) );
	}

	private List<Long> idsLies( String table, String colFiltre, Long val, String colRes ) {
		var sql = "SELECT " + colRes + " FROM " + table + " WHERE " + colFiltre + " = :v";
		return jdbc.queryForList( sql, new MapSqlParameterSource( "v", val ), Long.class );
	}

	// --- Avoir : staff d'un hackathon ---
	public void ajouterStaff( Long idHack, Long idMemstaf )  { lier( "avoir", "id_hack", idHack, "id_memstaf", idMemstaf ); }
	public void retirerStaff( Long idHack, Long idMemstaf )  { delier( "avoir", "id_hack", idHack, "id_memstaf", idMemstaf ); }
	public List<Long> staffDuHackathon( Long idHack )        { return idsLies( "avoir", "id_hack", idHack, "id_memstaf" ); }

	// --- Contenir : épreuves d'un hackathon ---
	public void ajouterEpreuve( Long idHack, Long idEpreuve ) { lier( "contenir", "id_hack", idHack, "id_epreuve", idEpreuve ); }
	public void retirerEpreuve( Long idHack, Long idEpreuve ) { delier( "contenir", "id_hack", idHack, "id_epreuve", idEpreuve ); }
	public List<Long> epreuvesDuHackathon( Long idHack )      { return idsLies( "contenir", "id_hack", idHack, "id_epreuve" ); }

	// --- Sponsoriser : partenaires d'un hackathon ---
	public void ajouterPartenaire( Long idHack, Long idPartenaire ) { lier( "sponsoriser", "id_hack", idHack, "id_partenaire", idPartenaire ); }
	public void retirerPartenaire( Long idHack, Long idPartenaire ) { delier( "sponsoriser", "id_hack", idHack, "id_partenaire", idPartenaire ); }
	public List<Long> partenairesDuHackathon( Long idHack )         { return idsLies( "sponsoriser", "id_hack", idHack, "id_partenaire" ); }

}

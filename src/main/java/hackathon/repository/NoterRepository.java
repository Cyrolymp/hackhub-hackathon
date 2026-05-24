package hackathon.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import hackathon.domain.Noter;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoterRepository {

	private final NamedParameterJdbcTemplate jdbc;

	// Insère ou met à jour la note d'une équipe par un juge
	public void upsert( Long idEquipe, Long idJuge, BigDecimal note ) {
		var sql = "INSERT INTO noter (id_equipe, id_juge, note) "
				+ "VALUES (:idEquipe, :idJuge, :note) "
				+ "ON CONFLICT (id_equipe, id_juge) DO UPDATE SET note = EXCLUDED.note";
		var p = new MapSqlParameterSource()
				.addValue( "idEquipe", idEquipe )
				.addValue( "idJuge", idJuge )
				.addValue( "note", note );
		jdbc.update( sql, p );
	}

	// Toutes les notes d'une équipe
	public List<Noter> findByEquipe( Long idEquipe ) {
		var sql = "SELECT id_equipe, id_juge, note FROM noter WHERE id_equipe = :idEquipe";
		var p = new MapSqlParameterSource( "idEquipe", idEquipe );
		return jdbc.query( sql, p, ( rs, n ) -> new Noter(
				rs.getLong( "id_equipe" ), rs.getLong( "id_juge" ), rs.getBigDecimal( "note" ) ) );
	}

	// Toutes les notes attribuées par un juge
	public List<Noter> findByJuge( Long idJuge ) {
		var sql = "SELECT id_equipe, id_juge, note FROM noter WHERE id_juge = :idJuge";
		var p = new MapSqlParameterSource( "idJuge", idJuge );
		return jdbc.query( sql, p, ( rs, n ) -> new Noter(
				rs.getLong( "id_equipe" ), rs.getLong( "id_juge" ), rs.getBigDecimal( "note" ) ) );
	}

	// Moyenne des notes d'une équipe (null si aucune note)
	public BigDecimal moyenneEquipe( Long idEquipe ) {
		var sql = "SELECT AVG(note) FROM noter WHERE id_equipe = :idEquipe";
		var p = new MapSqlParameterSource( "idEquipe", idEquipe );
		return jdbc.queryForObject( sql, p, BigDecimal.class );
	}

	// Supprime une note
	public void delete( Long idEquipe, Long idJuge ) {
		var sql = "DELETE FROM noter WHERE id_equipe = :idEquipe AND id_juge = :idJuge";
		var p = new MapSqlParameterSource()
				.addValue( "idEquipe", idEquipe )
				.addValue( "idJuge", idJuge );
		jdbc.update( sql, p );
	}

}

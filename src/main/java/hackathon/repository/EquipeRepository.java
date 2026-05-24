package hackathon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Equipe;

public interface EquipeRepository
		extends ListCrudRepository<Equipe, Long>, ListPagingAndSortingRepository<Equipe, Long> {

	Page<Equipe> findByNomEquipeContainingIgnoreCase( String search, Pageable pageable );

	List<Equipe> findByIdHack( Long idHack );

	Equipe findByCodeEquipe( String codeEquipe );

}

package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Juge;

public interface JugeRepository
		extends ListCrudRepository<Juge, Long>, ListPagingAndSortingRepository<Juge, Long> {

	Page<Juge> findByNomJugeContainingIgnoreCase( String search, Pageable pageable );

	boolean existsByIdCompte( Long idCompte );

}

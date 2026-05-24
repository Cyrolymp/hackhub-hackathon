package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Epreuve;

public interface EpreuveRepository
		extends ListCrudRepository<Epreuve, Long>, ListPagingAndSortingRepository<Epreuve, Long> {

	Page<Epreuve> findByNomEpreuveContainingIgnoreCase( String search, Pageable pageable );

}

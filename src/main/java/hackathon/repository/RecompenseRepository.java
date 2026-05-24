package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Recompense;

public interface RecompenseRepository
		extends ListCrudRepository<Recompense, Long>, ListPagingAndSortingRepository<Recompense, Long> {

	Page<Recompense> findByNomRecompenseContainingIgnoreCase( String search, Pageable pageable );

}

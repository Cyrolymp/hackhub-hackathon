package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Partenaire;

public interface PartenaireRepository
		extends ListCrudRepository<Partenaire, Long>, ListPagingAndSortingRepository<Partenaire, Long> {

	Page<Partenaire> findByNomPartenaireContainingIgnoreCase( String search, Pageable pageable );

	boolean existsByIdCompte( Long idCompte );

}

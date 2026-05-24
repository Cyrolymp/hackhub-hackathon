package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.OrganisateurHackathon;

public interface OrganisateurHackathonRepository
		extends ListCrudRepository<OrganisateurHackathon, Long>,
		ListPagingAndSortingRepository<OrganisateurHackathon, Long> {

	Page<OrganisateurHackathon> findByNomOrgContainingIgnoreCase( String search, Pageable pageable );

	boolean existsByIdCompte( Long idCompte );

}

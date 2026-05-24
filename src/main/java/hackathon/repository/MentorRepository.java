package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Mentor;

public interface MentorRepository
		extends ListCrudRepository<Mentor, Long>, ListPagingAndSortingRepository<Mentor, Long> {

	Page<Mentor> findByNomMentorContainingIgnoreCase( String search, Pageable pageable );

	boolean existsByIdCompte( Long idCompte );

}

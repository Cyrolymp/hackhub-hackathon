package hackathon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Participant;

public interface ParticipantRepository
		extends ListCrudRepository<Participant, Long>, ListPagingAndSortingRepository<Participant, Long> {

	Page<Participant> findByNomParticipantContainingIgnoreCase( String search, Pageable pageable );

	List<Participant> findByIdEquipe( Long idEquipe );

	boolean existsByIdCompte( Long idCompte );

}

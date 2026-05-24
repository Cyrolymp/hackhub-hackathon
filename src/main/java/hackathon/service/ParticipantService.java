package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Participant;
import hackathon.repository.ParticipantRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantService {

	private final ParticipantRepository participantRepository;

	public Page<Participant> getPage( Paging paging ) {
		Page<Participant> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomParticipant" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = participantRepository.findAll( pageable );
			} else {
				page = participantRepository.findByNomParticipantContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Participant findById( Long id ) {
		return participantRepository.findById( id ).orElseThrow();
	}

	public Participant save( Participant item ) {
		return participantRepository.save( item );
	}

	public void delete( Long id ) {
		participantRepository.deleteById( id );
	}

}

package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Mentor;
import hackathon.repository.MentorRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MentorService {

	private final MentorRepository mentorRepository;

	public Page<Mentor> getPage( Paging paging ) {
		Page<Mentor> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomMentor" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = mentorRepository.findAll( pageable );
			} else {
				page = mentorRepository.findByNomMentorContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Mentor findById( Long id ) {
		return mentorRepository.findById( id ).orElseThrow();
	}

	public Mentor save( Mentor item ) {
		return mentorRepository.save( item );
	}

	public void delete( Long id ) {
		mentorRepository.deleteById( id );
	}

}

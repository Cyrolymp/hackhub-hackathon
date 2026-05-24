package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Hackathon;
import hackathon.repository.HackathonRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HackathonService {

	private final HackathonRepository hackathonRepository;

	public Page<Hackathon> getPage( Paging paging ) {
		Page<Hackathon> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomHack" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = hackathonRepository.findAll( pageable );
			} else {
				page = hackathonRepository.findByNomHackContainingIgnoreCaseOrThemeHachContainingIgnoreCase( paging.getSearch(), paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Hackathon findById( Long id ) {
		return hackathonRepository.findById( id ).orElseThrow();
	}

	public Hackathon save( Hackathon item ) {
		return hackathonRepository.save( item );
	}

	public void delete( Long id ) {
		hackathonRepository.deleteById( id );
	}

}

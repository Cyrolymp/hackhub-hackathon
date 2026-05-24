package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Epreuve;
import hackathon.repository.EpreuveRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EpreuveService {

	private final EpreuveRepository epreuveRepository;

	public Page<Epreuve> getPage( Paging paging ) {
		Page<Epreuve> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomEpreuve" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = epreuveRepository.findAll( pageable );
			} else {
				page = epreuveRepository.findByNomEpreuveContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Epreuve findById( Long id ) {
		return epreuveRepository.findById( id ).orElseThrow();
	}

	public Epreuve save( Epreuve item ) {
		return epreuveRepository.save( item );
	}

	public void delete( Long id ) {
		epreuveRepository.deleteById( id );
	}

}

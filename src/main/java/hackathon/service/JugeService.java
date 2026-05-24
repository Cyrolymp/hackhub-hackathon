package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Juge;
import hackathon.repository.JugeRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JugeService {

	private final JugeRepository jugeRepository;

	public Page<Juge> getPage( Paging paging ) {
		Page<Juge> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomJuge" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = jugeRepository.findAll( pageable );
			} else {
				page = jugeRepository.findByNomJugeContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Juge findById( Long id ) {
		return jugeRepository.findById( id ).orElseThrow();
	}

	public Juge save( Juge item ) {
		return jugeRepository.save( item );
	}

	public void delete( Long id ) {
		jugeRepository.deleteById( id );
	}

}

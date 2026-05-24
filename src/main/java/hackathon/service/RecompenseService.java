package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Recompense;
import hackathon.repository.RecompenseRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecompenseService {

	private final RecompenseRepository recompenseRepository;

	public Page<Recompense> getPage( Paging paging ) {
		Page<Recompense> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomRecompense" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = recompenseRepository.findAll( pageable );
			} else {
				page = recompenseRepository.findByNomRecompenseContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Recompense findById( Long id ) {
		return recompenseRepository.findById( id ).orElseThrow();
	}

	public Recompense save( Recompense item ) {
		return recompenseRepository.save( item );
	}

	public void delete( Long id ) {
		recompenseRepository.deleteById( id );
	}

}

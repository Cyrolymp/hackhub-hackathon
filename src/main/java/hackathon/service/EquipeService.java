package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Equipe;
import hackathon.repository.EquipeRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipeService {

	private final EquipeRepository equipeRepository;

	public Page<Equipe> getPage( Paging paging ) {
		Page<Equipe> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomEquipe" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = equipeRepository.findAll( pageable );
			} else {
				page = equipeRepository.findByNomEquipeContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Equipe findById( Long id ) {
		return equipeRepository.findById( id ).orElseThrow();
	}

	public Equipe save( Equipe item ) {
		return equipeRepository.save( item );
	}

	public void delete( Long id ) {
		equipeRepository.deleteById( id );
	}

}

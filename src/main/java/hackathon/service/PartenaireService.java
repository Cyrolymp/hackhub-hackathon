package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.Partenaire;
import hackathon.repository.PartenaireRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartenaireService {

	private final PartenaireRepository partenaireRepository;

	public Page<Partenaire> getPage( Paging paging ) {
		Page<Partenaire> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomPartenaire" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = partenaireRepository.findAll( pageable );
			} else {
				page = partenaireRepository.findByNomPartenaireContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public Partenaire findById( Long id ) {
		return partenaireRepository.findById( id ).orElseThrow();
	}

	public Partenaire save( Partenaire item ) {
		return partenaireRepository.save( item );
	}

	public void delete( Long id ) {
		partenaireRepository.deleteById( id );
	}

}

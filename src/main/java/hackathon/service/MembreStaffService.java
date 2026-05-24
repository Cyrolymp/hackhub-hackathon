package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.MembreStaff;
import hackathon.repository.MembreStaffRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembreStaffService {

	private final MembreStaffRepository membreStaffRepository;

	public Page<MembreStaff> getPage( Paging paging ) {
		Page<MembreStaff> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomMenstaf" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = membreStaffRepository.findAll( pageable );
			} else {
				page = membreStaffRepository.findByNomMenstafContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public MembreStaff findById( Long id ) {
		return membreStaffRepository.findById( id ).orElseThrow();
	}

	public MembreStaff save( MembreStaff item ) {
		return membreStaffRepository.save( item );
	}

	public void delete( Long id ) {
		membreStaffRepository.deleteById( id );
	}

}

package hackathon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hackathon.domain.OrganisateurHackathon;
import hackathon.repository.OrganisateurHackathonRepository;
import hackathon.util.Paging;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganisateurHackathonService {

	private final OrganisateurHackathonRepository organisateurHackathonRepository;

	public Page<OrganisateurHackathon> getPage( Paging paging ) {
		Page<OrganisateurHackathon> page;
		while ( true ) {
			var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize(),
					Sort.by( "nomOrg" ) );
			if ( paging.getSearch() == null || paging.getSearch().isBlank() ) {
				page = organisateurHackathonRepository.findAll( pageable );
			} else {
				page = organisateurHackathonRepository.findByNomOrgContainingIgnoreCase( paging.getSearch(), pageable );
			}
			if ( paging.getPageNum() <= page.getTotalPages() || page.getTotalPages() == 0 ) {
				paging.setTotalPages( page.getTotalPages() );
				return page;
			}
			paging.setPageNum( page.getTotalPages() );
		}
	}

	public OrganisateurHackathon findById( Long id ) {
		return organisateurHackathonRepository.findById( id ).orElseThrow();
	}

	public OrganisateurHackathon save( OrganisateurHackathon item ) {
		return organisateurHackathonRepository.save( item );
	}

	public void delete( Long id ) {
		organisateurHackathonRepository.deleteById( id );
	}

}

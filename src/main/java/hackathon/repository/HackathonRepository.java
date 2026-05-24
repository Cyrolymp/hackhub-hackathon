package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.Hackathon;

public interface HackathonRepository
		extends ListCrudRepository<Hackathon, Long>, ListPagingAndSortingRepository<Hackathon, Long> {

	Page<Hackathon> findByNomHackContainingIgnoreCaseOrThemeHachContainingIgnoreCase(
			String nom, String theme, Pageable pageable );

}

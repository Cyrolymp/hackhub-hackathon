package hackathon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import hackathon.domain.MembreStaff;

public interface MembreStaffRepository
		extends ListCrudRepository<MembreStaff, Long>, ListPagingAndSortingRepository<MembreStaff, Long> {

	Page<MembreStaff> findByNomMenstafContainingIgnoreCase( String search, Pageable pageable );

}

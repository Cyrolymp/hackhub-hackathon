package com.hackhub.repository;

import com.hackhub.entity.Hackathon;
import com.hackhub.entity.StatutHackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {
    List<Hackathon> findByOrganisateurId(Long organisateurId);
    List<Hackathon> findByStatut(StatutHackathon statut);
    List<Hackathon> findByStatutNot(StatutHackathon statut);

    @Query("SELECT h FROM Hackathon h WHERE " +
           "LOWER(h.titre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(h.categorie) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(h.lieu) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Hackathon> search(@Param("q") String query);
}

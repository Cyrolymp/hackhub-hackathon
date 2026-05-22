package com.hackhub.repository;

import com.hackhub.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    Optional<Equipe> findByCode(String code);
    boolean existsByCode(String code);
    Optional<Equipe> findByChefId(Long chefId);
}

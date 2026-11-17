package com.uasz.ms_patrimoine.repositories;

import com.uasz.ms_patrimoine.entities.Patrimoine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatrimoineRepository extends JpaRepository<Patrimoine, Long> {
    List<Patrimoine> findByNomContainingIgnoreCase(String motCle);
    List<Patrimoine> findByCategorieId(Long categorieId);
    Optional<Patrimoine> findByNom(String nom);
}

package com.uasz.ms_patrimoine.repositories;

import com.uasz.ms_patrimoine.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByPatrimoineId(Long patrimoineId);
}

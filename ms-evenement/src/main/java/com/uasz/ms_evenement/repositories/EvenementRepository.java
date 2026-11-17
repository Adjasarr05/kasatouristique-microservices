package com.uasz.ms_evenement.repositories;

import com.uasz.ms_evenement.entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByDateHeureDebutAfterOrderByDateHeureDebutAsc(LocalDateTime maintenant);
    List<Evenement> findByPatrimoineId(Long patrimoineId);
    List<Evenement> findByCircuitId(Long circuitId);
}

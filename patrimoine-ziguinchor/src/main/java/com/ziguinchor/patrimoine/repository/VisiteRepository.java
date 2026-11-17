package com.ziguinchor.patrimoine.repository;

import com.ziguinchor.patrimoine.model.Visite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface VisiteRepository extends JpaRepository<Visite, Long> {
    List<Visite> findByDateHeureDebutAfterOrderByDateHeureDebutAsc(LocalDateTime maintenant);
    List<Visite> findByDateHeureDebutBetweenOrderByDateHeureDebutAsc(LocalDateTime debut, LocalDateTime fin);
    List<Visite> findByGuideContainingIgnoreCaseAndDateHeureDebutBetweenOrderByDateHeureDebutAsc(
            String guide, LocalDateTime debut, LocalDateTime fin);
    List<Visite> findByGuideContainingIgnoreCaseOrderByDateHeureDebutAsc(String guide);
}

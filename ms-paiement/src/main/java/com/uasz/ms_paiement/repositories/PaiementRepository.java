package com.uasz.ms_paiement.repositories;

import com.uasz.ms_paiement.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByReservationId(Long reservationId);
    List<Paiement> findByUtilisateurId(Long utilisateurId);
}

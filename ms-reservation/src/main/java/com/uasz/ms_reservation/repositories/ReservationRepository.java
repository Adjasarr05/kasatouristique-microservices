package com.uasz.ms_reservation.repositories;

import com.uasz.ms_reservation.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUtilisateurIdOrderByDateReservationDesc(Long utilisateurId);
    List<Reservation> findByEvenementId(Long evenementId);
    List<Reservation> findByCircuitId(Long circuitId);
}

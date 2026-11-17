package com.uasz.ms_reservation.services;

import com.uasz.ms_reservation.entities.Reservation;

import java.util.List;
import java.util.Optional;

public interface IReservationService {
    Reservation saveReservation(Reservation reservation);

    List<Reservation> getAllReservations();

    List<Reservation> getByUtilisateurId(Long utilisateurId);

    Optional<Reservation> getByIdReservation(Long id);

    Reservation updateReservation(Long id, Reservation newReservation);

    Reservation annulerReservation(Long id);

    Reservation confirmerReservation(Long id);

    void deleteReservation(Long id);
}

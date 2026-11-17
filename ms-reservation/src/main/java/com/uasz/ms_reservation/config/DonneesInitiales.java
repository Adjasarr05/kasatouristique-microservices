package com.uasz.ms_reservation.config;

import com.uasz.ms_reservation.entities.Reservation;
import com.uasz.ms_reservation.entities.StatutReservation;
import com.uasz.ms_reservation.repositories.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    public DonneesInitiales(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        if (reservationRepository.count() > 0) {
            return;
        }

        Reservation r1 = new Reservation();
        r1.setUtilisateurId(5L);
        r1.setUtilisateurNom("Visiteur Test");
        r1.setUtilisateurEmail("visiteur@ziguinchor.sn");
        r1.setTypeReservation("EVENEMENT");
        r1.setEvenementId(1L);
        r1.setCircuitId(1L);
        r1.setNomCible("Visite guidée : Cathédrale Saint Antoine");
        r1.setDateHeureCible(LocalDateTime.now().plusDays(3).withHour(9).withMinute(0));
        r1.setNombrePlaces(1);
        r1.setStatut(StatutReservation.CONFIRME);
        r1.setMontant(15000.0);
        reservationRepository.save(r1);

        Reservation r2 = new Reservation();
        r2.setUtilisateurId(5L);
        r2.setUtilisateurNom("Visiteur Test");
        r2.setUtilisateurEmail("visiteur@ziguinchor.sn");
        r2.setTypeReservation("EVENEMENT");
        r2.setEvenementId(5L);
        r2.setNomCible("Visite historique : Cathédrale Saint Antoine (session passée)");
        r2.setDateHeureCible(LocalDateTime.now().minusDays(2).withHour(10).withMinute(0));
        r2.setNombrePlaces(1);
        r2.setStatut(StatutReservation.CONFIRME);
        r2.setMontant(5000.0);
        reservationRepository.save(r2);
    }
}

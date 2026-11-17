package com.uasz.ms_paiement.services;

import com.uasz.ms_paiement.entities.Paiement;

import java.util.List;
import java.util.Optional;

public interface IPaiementService {
    Paiement savePaiement(Paiement paiement);

    List<Paiement> getAllPaiements();

    List<Paiement> getByReservationId(Long reservationId);

    List<Paiement> getByUtilisateurId(Long utilisateurId);

    Optional<Paiement> getByIdPaiement(Long id);

    Paiement rembourserPaiement(Long id);

    void deletePaiement(Long id);
}

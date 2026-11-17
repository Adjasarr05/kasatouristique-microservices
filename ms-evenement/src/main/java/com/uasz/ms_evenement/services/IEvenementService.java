package com.uasz.ms_evenement.services;

import com.uasz.ms_evenement.entities.Evenement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IEvenementService {
    Evenement saveEvenement(Evenement evenement);

    List<Evenement> getAllEvenements();

    List<Evenement> getEvenementsAVenir();

    List<Evenement> getByPatrimoineId(Long patrimoineId);

    List<Evenement> getByCircuitId(Long circuitId);

    Optional<Evenement> getByIdEvenement(Long id);

    Evenement updateEvenement(Long id, Evenement newEvenement);

    void deleteEvenement(Long id);

    Map<String, Object> verifierDisponibilite(Long id);

    Evenement reserverPlace(Long id, int quantite);

    Evenement libererPlace(Long id, int quantite);
}

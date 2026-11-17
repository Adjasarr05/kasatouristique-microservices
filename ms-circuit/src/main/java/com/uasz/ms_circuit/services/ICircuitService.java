package com.uasz.ms_circuit.services;

import com.uasz.ms_circuit.entities.Circuit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICircuitService {
    Circuit saveCircuit(Circuit circuit);

    List<Circuit> getAllCircuits();

    List<Circuit> getCircuitsPublies();

    List<Circuit> getByOperateurId(Long operateurId);

    Optional<Circuit> getByIdCircuit(Long id);

    Circuit updateCircuit(Long id, Circuit newCircuit);

    void deleteCircuit(Long id);

    Circuit publierCircuit(Long id);

    Map<String, Object> verifierDisponibilite(Long id);

    Circuit reserverPlace(Long id, int quantite);

    Circuit libererPlace(Long id, int quantite);
}

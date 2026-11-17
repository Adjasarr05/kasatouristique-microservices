package com.uasz.ms_circuit.services;

import com.uasz.ms_circuit.entities.Circuit;
import com.uasz.ms_circuit.entities.EtapeCircuit;
import com.uasz.ms_circuit.entities.StatutCircuit;
import com.uasz.ms_circuit.repositories.CircuitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CircuitService implements ICircuitService {

    private static final Logger log = LoggerFactory.getLogger(CircuitService.class);

    private final CircuitRepository circuitRepository;
    private final RestTemplate restTemplate;

    @Value("${microservices.patrimoine.url:http://localhost:8081}")
    private String patrimoineServiceUrl;

    public CircuitService(CircuitRepository circuitRepository, RestTemplate restTemplate) {
        this.circuitRepository = circuitRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Circuit saveCircuit(Circuit circuit) {
        if (circuit.getCapaciteMax() <= 0) {
            circuit.setCapaciteMax(20);
        }
        if (circuit.getStatut() == null) {
            circuit.setStatut(StatutCircuit.BROUILLON);
        }
        if (circuit.getEtapes() != null) {
            for (EtapeCircuit etape : circuit.getEtapes()) {
                etape.setCircuit(circuit);
                completerEtapeDepuisPatrimoine(etape);
            }
        }
        return circuitRepository.save(circuit);
    }

    private void completerEtapeDepuisPatrimoine(EtapeCircuit etape) {
        if (etape.getPatrimoineId() == null) {
            return;
        }
        if (etape.getNomEtape() != null && !etape.getNomEtape().isBlank()) {
            return;
        }
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    patrimoineServiceUrl + "/api/patrimoines/" + etape.getPatrimoineId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null) {
                Object nom = body.get("nom") != null ? body.get("nom") : body.get("nomPatrimoine");
                if (nom != null) {
                    etape.setNomEtape(nom.toString());
                }
                if ((etape.getDescription() == null || etape.getDescription().isBlank())
                        && body.get("description") != null) {
                    etape.setDescription(body.get("description").toString());
                }
            }
        } catch (Exception e) {
            log.warn("ms-patrimoine inaccessible pour l'étape patrimoineId={} : {}", etape.getPatrimoineId(), e.getMessage());
        }
    }

    @Override
    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }

    @Override
    public List<Circuit> getCircuitsPublies() {
        return circuitRepository.findByStatut(StatutCircuit.PUBLIE);
    }

    @Override
    public List<Circuit> getByOperateurId(Long operateurId) {
        return circuitRepository.findByOperateurId(operateurId);
    }

    @Override
    public Optional<Circuit> getByIdCircuit(Long id) {
        return circuitRepository.findById(id);
    }

    @Override
    public Circuit updateCircuit(Long id, Circuit newCircuit) {
        return circuitRepository.findById(id).map(circuit -> {
            circuit.setNom(newCircuit.getNom());
            circuit.setDescription(newCircuit.getDescription());
            circuit.setDureeJours(newCircuit.getDureeJours());
            circuit.setDureeEstimee(newCircuit.getDureeEstimee());
            circuit.setDistanceKm(newCircuit.getDistanceKm());
            circuit.setDifficulte(newCircuit.getDifficulte());
            circuit.setPrix(newCircuit.getPrix());
            if (newCircuit.getStatut() != null) {
                circuit.setStatut(newCircuit.getStatut());
            }
            if (newCircuit.getCapaciteMax() > 0) {
                circuit.setCapaciteMax(newCircuit.getCapaciteMax());
            }
            if (newCircuit.getOperateurId() != null) {
                circuit.setOperateurId(newCircuit.getOperateurId());
            }
            if (newCircuit.getOperateurNom() != null) {
                circuit.setOperateurNom(newCircuit.getOperateurNom());
            }
            if (newCircuit.getPatrimoineIds() != null) {
                circuit.getPatrimoineIds().clear();
                circuit.getPatrimoineIds().addAll(newCircuit.getPatrimoineIds());
            }
            if (newCircuit.getEtapes() != null) {
                circuit.getEtapes().clear();
                for (EtapeCircuit etape : newCircuit.getEtapes()) {
                    etape.setCircuit(circuit);
                    circuit.getEtapes().add(etape);
                }
            }
            return circuitRepository.save(circuit);
        }).orElseThrow(() -> new RuntimeException("Circuit non trouvé avec l'id : " + id));
    }

    @Override
    public void deleteCircuit(Long id) {
        circuitRepository.deleteById(id);
    }

    @Override
    public Circuit publierCircuit(Long id) {
        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé avec l'id : " + id));
        circuit.setStatut(StatutCircuit.PUBLIE);
        return circuitRepository.save(circuit);
    }

    @Override
    public Map<String, Object> verifierDisponibilite(Long id) {
        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé avec l'id : " + id));
        Map<String, Object> result = new HashMap<>();
        result.put("circuitId", circuit.getId());
        result.put("nom", circuit.getNom());
        result.put("disponible", circuit.isDisponible());
        result.put("placesDisponibles", circuit.getPlacesDisponibles());
        result.put("capaciteMax", circuit.getCapaciteMax());
        result.put("placesReservees", circuit.getPlacesReservees());
        result.put("prix", circuit.getPrix());
        result.put("statut", circuit.getStatut().name());
        return result;
    }

    @Override
    public Circuit reserverPlace(Long id, int quantite) {
        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé avec l'id : " + id));
        if (circuit.getStatut() != StatutCircuit.PUBLIE) {
            throw new RuntimeException("Ce circuit n'est pas encore publié.");
        }
        if (circuit.getPlacesDisponibles() < quantite) {
            throw new RuntimeException("Places insuffisantes sur ce circuit.");
        }
        circuit.setPlacesReservees(circuit.getPlacesReservees() + quantite);
        return circuitRepository.save(circuit);
    }

    @Override
    public Circuit libererPlace(Long id, int quantite) {
        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit non trouvé avec l'id : " + id));
        circuit.setPlacesReservees(Math.max(0, circuit.getPlacesReservees() - quantite));
        return circuitRepository.save(circuit);
    }
}

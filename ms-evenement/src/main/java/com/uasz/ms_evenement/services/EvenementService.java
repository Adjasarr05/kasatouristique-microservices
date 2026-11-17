package com.uasz.ms_evenement.services;

import com.uasz.ms_evenement.entities.Evenement;
import com.uasz.ms_evenement.entities.StatutEvenement;
import com.uasz.ms_evenement.repositories.EvenementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EvenementService implements IEvenementService {

    private static final Logger log = LoggerFactory.getLogger(EvenementService.class);

    private final EvenementRepository evenementRepository;
    private final RestTemplate restTemplate;

    @Value("${microservices.patrimoine.url:http://localhost:8081}")
    private String patrimoineServiceUrl;

    @Value("${microservices.circuit.url:http://localhost:8082}")
    private String circuitServiceUrl;

    public EvenementService(EvenementRepository evenementRepository, RestTemplate restTemplate) {
        this.evenementRepository = evenementRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Evenement saveEvenement(Evenement evenement) {
        if (evenement.getCapaciteMax() <= 0) {
            evenement.setCapaciteMax(15);
        }
        if (evenement.getDureeMinutes() <= 0) {
            evenement.setDureeMinutes(60);
        }
        if (evenement.getPrix() == null) {
            evenement.setPrix(0.0);
        }
        if (evenement.getStatut() == null) {
            evenement.setStatut(StatutEvenement.PLANIFIEE);
        }
        if (evenement.getTypeEvenement() == null || evenement.getTypeEvenement().isBlank()) {
            evenement.setTypeEvenement("VISITE_GUIDEE");
        }
        enrichirDepuisPatrimoine(evenement);
        enrichirDepuisCircuit(evenement);
        return evenementRepository.save(evenement);
    }

    private void enrichirDepuisPatrimoine(Evenement evenement) {
        if (evenement.getPatrimoineId() == null) {
            return;
        }
        if (evenement.getPatrimoineNom() != null && !evenement.getPatrimoineNom().isBlank()) {
            return;
        }
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    patrimoineServiceUrl + "/api/patrimoines/" + evenement.getPatrimoineId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null) {
                Object nom = body.get("nom") != null ? body.get("nom") : body.get("nomPatrimoine");
                if (nom != null) {
                    evenement.setPatrimoineNom(nom.toString());
                }
                if ((evenement.getLieuPrecis() == null || evenement.getLieuPrecis().isBlank())
                        && body.get("adresse") != null) {
                    evenement.setLieuPrecis(body.get("adresse").toString());
                }
            }
        } catch (Exception e) {
            log.warn("ms-patrimoine inaccessible pour patrimoineId={} : {}", evenement.getPatrimoineId(), e.getMessage());
        }
    }

    private void enrichirDepuisCircuit(Evenement evenement) {
        if (evenement.getCircuitId() == null) {
            return;
        }
        if (evenement.getCircuitNom() != null && !evenement.getCircuitNom().isBlank()) {
            return;
        }
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    circuitServiceUrl + "/api/circuits/" + evenement.getCircuitId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null) {
                if (body.get("nom") != null) {
                    evenement.setCircuitNom(body.get("nom").toString());
                }
                if ((evenement.getPrix() == null || evenement.getPrix() == 0.0)
                        && body.get("prix") instanceof Number p) {
                    evenement.setPrix(p.doubleValue());
                }
            }
        } catch (Exception e) {
            log.warn("ms-circuit inaccessible pour circuitId={} : {}", evenement.getCircuitId(), e.getMessage());
        }
    }

    @Override
    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    @Override
    public List<Evenement> getEvenementsAVenir() {
        return evenementRepository.findByDateHeureDebutAfterOrderByDateHeureDebutAsc(LocalDateTime.now());
    }

    @Override
    public List<Evenement> getByPatrimoineId(Long patrimoineId) {
        return evenementRepository.findByPatrimoineId(patrimoineId);
    }

    @Override
    public List<Evenement> getByCircuitId(Long circuitId) {
        return evenementRepository.findByCircuitId(circuitId);
    }

    @Override
    public Optional<Evenement> getByIdEvenement(Long id) {
        return evenementRepository.findById(id);
    }

    @Override
    public Evenement updateEvenement(Long id, Evenement newEvenement) {
        return evenementRepository.findById(id).map(ev -> {
            ev.setTitre(newEvenement.getTitre());
            ev.setTypeEvenement(newEvenement.getTypeEvenement());
            ev.setDescription(newEvenement.getDescription());
            ev.setLieuPrecis(newEvenement.getLieuPrecis());
            ev.setPatrimoineId(newEvenement.getPatrimoineId());
            ev.setPatrimoineNom(newEvenement.getPatrimoineNom());
            ev.setCircuitId(newEvenement.getCircuitId());
            ev.setCircuitNom(newEvenement.getCircuitNom());
            ev.setGuide(newEvenement.getGuide());
            ev.setDateHeureDebut(newEvenement.getDateHeureDebut());
            ev.setDureeMinutes(newEvenement.getDureeMinutes());
            if (newEvenement.getCapaciteMax() > 0) {
                ev.setCapaciteMax(newEvenement.getCapaciteMax());
            }
            if (newEvenement.getPrix() != null) {
                ev.setPrix(newEvenement.getPrix());
            }
            if (newEvenement.getStatut() != null) {
                ev.setStatut(newEvenement.getStatut());
            }
            return evenementRepository.save(ev);
        }).orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id : " + id));
    }

    @Override
    public void deleteEvenement(Long id) {
        evenementRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> verifierDisponibilite(Long id) {
        Evenement ev = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id : " + id));
        Map<String, Object> result = new HashMap<>();
        result.put("evenementId", ev.getId());
        result.put("titre", ev.getTitre());
        result.put("disponible", !ev.isComplete());
        result.put("placesDisponibles", ev.getPlacesDisponibles());
        result.put("capaciteMax", ev.getCapaciteMax());
        result.put("placesReservees", ev.getPlacesReservees());
        result.put("prix", ev.getPrix() != null ? ev.getPrix() : 0.0);
        result.put("statut", ev.getStatut().name());
        return result;
    }

    @Override
    public Evenement reserverPlace(Long id, int quantite) {
        Evenement ev = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id : " + id));
        if (ev.getStatut() != StatutEvenement.PLANIFIEE) {
            throw new RuntimeException("Cet événement n'est plus ouvert aux réservations.");
        }
        if (ev.getPlacesDisponibles() < quantite) {
            throw new RuntimeException("Plus assez de places disponibles pour cet événement.");
        }
        ev.setPlacesReservees(ev.getPlacesReservees() + quantite);
        return evenementRepository.save(ev);
    }

    @Override
    public Evenement libererPlace(Long id, int quantite) {
        Evenement ev = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id : " + id));
        ev.setPlacesReservees(Math.max(0, ev.getPlacesReservees() - quantite));
        return evenementRepository.save(ev);
    }
}

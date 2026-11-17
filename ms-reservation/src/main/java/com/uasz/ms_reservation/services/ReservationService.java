package com.uasz.ms_reservation.services;

import com.uasz.ms_reservation.entities.Reservation;
import com.uasz.ms_reservation.entities.StatutReservation;
import com.uasz.ms_reservation.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;

    @Value("${microservices.circuit.url:http://localhost:8082}")
    private String circuitServiceUrl;

    @Value("${microservices.evenement.url:http://localhost:8083}")
    private String evenementServiceUrl;

    @Value("${microservices.utilisateur.url:http://localhost:8085}")
    private String utilisateurServiceUrl;

    @Value("${microservices.paiement.url:http://localhost:8086}")
    private String paiementServiceUrl;

    @Value("${microservices.notification.url:http://localhost:8087}")
    private String notificationServiceUrl;

    public ReservationService(ReservationRepository reservationRepository, RestTemplate restTemplate) {
        this.reservationRepository = reservationRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        int quantite = reservation.getNombrePlaces() > 0 ? reservation.getNombrePlaces() : 1;
        reservation.setNombrePlaces(quantite);

        completerDepuisUtilisateur(reservation);

        if (reservation.getEvenementId() != null) {
            reservation.setTypeReservation("EVENEMENT");
            verifierEtReserverEvenement(reservation, quantite);
        } else if (reservation.getCircuitId() != null) {
            reservation.setTypeReservation("CIRCUIT");
            verifierEtReserverCircuit(reservation, quantite);
        }

        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.CONFIRME);
        }

        Reservation saved = reservationRepository.save(reservation);

        if (saved.getMontant() != null && saved.getMontant() > 0) {
            declencherPaiement(saved);
        }

        envoyerNotification(
                saved.getUtilisateurId(),
                saved.getUtilisateurEmail(),
                "CONFIRMATION",
                "Confirmation de réservation #" + saved.getId(),
                "Votre réservation pour " + (saved.getNomCible() != null ? saved.getNomCible() : "le patrimoine")
                        + " est confirmée. Code billet : " + saved.getQrCodeHash()
        );

        return saved;
    }

    private void completerDepuisUtilisateur(Reservation reservation) {
        if (reservation.getUtilisateurId() == null) {
            return;
        }
        boolean manqueNom = reservation.getUtilisateurNom() == null || reservation.getUtilisateurNom().isBlank();
        boolean manqueEmail = reservation.getUtilisateurEmail() == null || reservation.getUtilisateurEmail().isBlank();
        if (!manqueNom && !manqueEmail) {
            return;
        }
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    utilisateurServiceUrl + "/api/utilisateurs/" + reservation.getUtilisateurId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null) {
                if (manqueNom && body.get("nom") != null) {
                    reservation.setUtilisateurNom(body.get("nom").toString());
                }
                if (manqueEmail && body.get("email") != null) {
                    reservation.setUtilisateurEmail(body.get("email").toString());
                }
            }
        } catch (Exception e) {
            log.warn("ms-utilisateur inaccessible pour utilisateurId={} : {}", reservation.getUtilisateurId(), e.getMessage());
        }
    }

    private void verifierEtReserverEvenement(Reservation reservation, int quantite) {
        String urlDispo = evenementServiceUrl + "/api/evenements/" + reservation.getEvenementId() + "/disponibilite";
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    urlDispo,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null) {
                boolean disponible = Boolean.TRUE.equals(body.get("disponible"));
                int placesDispo = body.get("placesDisponibles") instanceof Number n ? n.intValue() : 0;
                if (!disponible || placesDispo < quantite) {
                    throw new IllegalStateException("Plus de places disponibles pour cet événement.");
                }
                if (reservation.getNomCible() == null && body.get("titre") != null) {
                    reservation.setNomCible(body.get("titre").toString());
                }
                if ((reservation.getMontant() == null || reservation.getMontant() == 0.0)
                        && body.get("prix") instanceof Number p) {
                    reservation.setMontant(p.doubleValue() * quantite);
                }
            }
            String urlReserver = evenementServiceUrl + "/api/evenements/" + reservation.getEvenementId()
                    + "/reserver-place?quantite=" + quantite;
            restTemplate.postForEntity(urlReserver, null, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Événement introuvable dans ms-evenement (id=" + reservation.getEvenementId() + ")");
        } catch (ResourceAccessException e) {
            log.warn("ms-evenement inaccessible ({}) : réservation enregistrée en mode autonome.", e.getMessage());
        }
    }

    private void verifierEtReserverCircuit(Reservation reservation, int quantite) {
        String urlDispo = circuitServiceUrl + "/api/circuits/" + reservation.getCircuitId() + "/disponibilite";
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    urlDispo,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body != null) {
                boolean disponible = Boolean.TRUE.equals(body.get("disponible"));
                int placesDispo = body.get("placesDisponibles") instanceof Number n ? n.intValue() : 0;
                if (!disponible || placesDispo < quantite) {
                    throw new IllegalStateException("Plus de places disponibles pour ce circuit.");
                }
                if (reservation.getNomCible() == null && body.get("nom") != null) {
                    reservation.setNomCible(body.get("nom").toString());
                }
                if ((reservation.getMontant() == null || reservation.getMontant() == 0.0)
                        && body.get("prix") instanceof Number p) {
                    reservation.setMontant(p.doubleValue() * quantite);
                }
            }
            String urlReserver = circuitServiceUrl + "/api/circuits/" + reservation.getCircuitId()
                    + "/reserver-place?quantite=" + quantite;
            restTemplate.postForEntity(urlReserver, null, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Circuit introuvable dans ms-circuit (id=" + reservation.getCircuitId() + ")");
        } catch (ResourceAccessException e) {
            log.warn("ms-circuit inaccessible ({}) : réservation enregistrée en mode autonome.", e.getMessage());
        }
    }

    private void declencherPaiement(Reservation saved) {
        try {
            Map<String, Object> paiementPayload = new HashMap<>();
            paiementPayload.put("reservationId", saved.getId());
            paiementPayload.put("utilisateurId", saved.getUtilisateurId());
            paiementPayload.put("montant", saved.getMontant());
            paiementPayload.put("modePaiement", "MOBILE_MONEY");
            restTemplate.postForEntity(paiementServiceUrl + "/api/paiements", paiementPayload, Object.class);
        } catch (RestClientException e) {
            log.warn("ms-paiement inaccessible ({}) : paiement non synchronisé.", e.getMessage());
        }
    }

    private void envoyerNotification(Long utilisateurId, String email, String type, String sujet, String message) {
        try {
            Map<String, Object> notifPayload = new HashMap<>();
            notifPayload.put("utilisateurId", utilisateurId);
            notifPayload.put("destinataire", email != null ? email : "touriste@ziguinchor.sn");
            notifPayload.put("canal", "EMAIL");
            notifPayload.put("typeNotification", type);
            notifPayload.put("sujet", sujet);
            notifPayload.put("message", message);
            restTemplate.postForEntity(notificationServiceUrl + "/api/notifications", notifPayload, Object.class);
        } catch (RestClientException e) {
            log.warn("ms-notification inaccessible ({}) : notification non envoyée.", e.getMessage());
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getByUtilisateurId(Long utilisateurId) {
        return reservationRepository.findByUtilisateurIdOrderByDateReservationDesc(utilisateurId);
    }

    @Override
    public Optional<Reservation> getByIdReservation(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation updateReservation(Long id, Reservation newReservation) {
        return reservationRepository.findById(id).map(r -> {
            if (newReservation.getStatut() != null) {
                r.setStatut(newReservation.getStatut());
            }
            if (newReservation.getNombrePlaces() > 0) {
                r.setNombrePlaces(newReservation.getNombrePlaces());
            }
            if (newReservation.getMontant() != null) {
                r.setMontant(newReservation.getMontant());
            }
            if (newReservation.getNomCible() != null) {
                r.setNomCible(newReservation.getNomCible());
            }
            return reservationRepository.save(r);
        }).orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
    }

    @Override
    public Reservation annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));

        if (reservation.getStatut() != StatutReservation.ANNULE) {
            reservation.setStatut(StatutReservation.ANNULE);
            reservationRepository.save(reservation);

            int quantite = reservation.getNombrePlaces() > 0 ? reservation.getNombrePlaces() : 1;
            if (reservation.getEvenementId() != null) {
                try {
                    restTemplate.postForEntity(
                            evenementServiceUrl + "/api/evenements/" + reservation.getEvenementId()
                                    + "/liberer-place?quantite=" + quantite,
                            null, Object.class);
                } catch (RestClientException e) {
                    log.warn("ms-evenement inaccessible lors de l'annulation : {}", e.getMessage());
                }
            } else if (reservation.getCircuitId() != null) {
                try {
                    restTemplate.postForEntity(
                            circuitServiceUrl + "/api/circuits/" + reservation.getCircuitId()
                                    + "/liberer-place?quantite=" + quantite,
                            null, Object.class);
                } catch (RestClientException e) {
                    log.warn("ms-circuit inaccessible lors de l'annulation : {}", e.getMessage());
                }
            }

            envoyerNotification(
                    reservation.getUtilisateurId(),
                    reservation.getUtilisateurEmail(),
                    "ANNULATION",
                    "Annulation de réservation #" + reservation.getId(),
                    "Votre réservation #" + reservation.getId() + " a bien été annulée."
            );
        }

        return reservation;
    }

    @Override
    public Reservation confirmerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
        reservation.setStatut(StatutReservation.CONFIRME);
        Reservation saved = reservationRepository.save(reservation);

        envoyerNotification(
                saved.getUtilisateurId(),
                saved.getUtilisateurEmail(),
                "CONFIRMATION",
                "Réservation confirmée #" + saved.getId(),
                "Votre réservation #" + saved.getId() + " est maintenant confirmée."
        );
        return saved;
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

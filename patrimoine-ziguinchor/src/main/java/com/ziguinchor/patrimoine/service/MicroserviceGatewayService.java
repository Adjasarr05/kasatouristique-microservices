package com.ziguinchor.patrimoine.service;

import com.ziguinchor.patrimoine.model.*;
import com.ziguinchor.patrimoine.repository.CategorieRepository;
import com.ziguinchor.patrimoine.repository.CircuitRepository;
import com.ziguinchor.patrimoine.repository.SiteTouristiqueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MicroserviceGatewayService {

    private static final Logger log = LoggerFactory.getLogger(MicroserviceGatewayService.class);

    private final RestTemplate restTemplate;
    private final SiteTouristiqueRepository siteTouristiqueRepository;
    private final CategorieRepository categorieRepository;
    private final CircuitRepository circuitRepository;

    @Value("${microservices.patrimoine.url:http://localhost:8081}")
    private String patrimoineUrl;

    @Value("${microservices.circuit.url:http://localhost:8082}")
    private String circuitUrl;

    @Value("${microservices.evenement.url:http://localhost:8083}")
    private String evenementUrl;

    @Value("${microservices.reservation.url:http://localhost:8084}")
    private String reservationUrl;

    @Value("${microservices.utilisateur.url:http://localhost:8085}")
    private String utilisateurUrl;

    @Value("${microservices.paiement.url:http://localhost:8086}")
    private String paiementUrl;

    @Value("${microservices.notification.url:http://localhost:8087}")
    private String notificationUrl;

    public MicroserviceGatewayService(SiteTouristiqueRepository siteTouristiqueRepository,
                                      CategorieRepository categorieRepository,
                                      CircuitRepository circuitRepository) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1500);
        factory.setReadTimeout(2000);
        this.restTemplate = new RestTemplate(factory);
        this.siteTouristiqueRepository = siteTouristiqueRepository;
        this.categorieRepository = categorieRepository;
        this.circuitRepository = circuitRepository;
    }

    public void importerPatrimoinesDepuisMicroservice() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    patrimoineUrl + "/api/patrimoines",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            List<Map<String, Object>> items = response.getBody();
            if (items == null) {
                return;
            }
            List<Categorie> categories = categorieRepository.findAll();
            for (Map<String, Object> item : items) {
                Object rawNom = item.get("nom") != null ? item.get("nom") : item.get("nomPatrimoine");
                if (rawNom == null || rawNom.toString().isBlank()) {
                    continue;
                }
                String nom = rawNom.toString();
                var existant = siteTouristiqueRepository.findByNom(nom);
                if (existant.isPresent()) {
                    SiteTouristique siteExistant = existant.get();
                    if ((siteExistant.getImageUrl() == null || siteExistant.getImageUrl().isBlank())
                            && item.get("imageUrl") != null) {
                        siteExistant.setImageUrl(item.get("imageUrl").toString());
                        siteTouristiqueRepository.save(siteExistant);
                    }
                    continue;
                }

                SiteTouristique site = new SiteTouristique();
                site.setNom(nom);
                Object rawDesc = item.get("description") != null
                        ? item.get("description")
                        : item.get("descriptionPatrimoine");
                String description = rawDesc != null && !rawDesc.toString().isBlank()
                        ? rawDesc.toString()
                        : "Site patrimonial de Ziguinchor";
                site.setDescription(description);

                Object rawAdresse = item.get("adresse") != null
                        ? item.get("adresse")
                        : item.get("adressePatrimoine");
                if (rawAdresse != null) {
                    site.setAdresse(rawAdresse.toString());
                }
                if (item.get("quartier") != null) {
                    site.setQuartier(item.get("quartier").toString());
                }
                if (item.get("imageUrl") != null) {
                    site.setImageUrl(item.get("imageUrl").toString());
                }
                if (item.get("latitude") instanceof Number lat) {
                    site.setLatitude(lat.doubleValue());
                }
                if (item.get("longitude") instanceof Number lon) {
                    site.setLongitude(lon.doubleValue());
                }
                if (item.get("categorie") instanceof Map<?, ?> catMap && catMap.get("nom") != null) {
                    String catNom = catMap.get("nom").toString();
                    categories.stream()
                            .filter(c -> c.getNom() != null && c.getNom().equalsIgnoreCase(catNom))
                            .findFirst()
                            .ifPresent(site::setCategorie);
                }
                siteTouristiqueRepository.save(site);
            }
        } catch (Exception e) {
            log.debug("ms-patrimoine hors ligne lors de l'import : {}", e.getMessage());
        }
    }

    public static class MicroserviceStatut {
        private final String nom;
        private final int port;
        private final String baseUrl;
        private final String endpoint;
        private final String description;
        private final boolean enLigne;
        private final int nombreEnregistrements;

        public MicroserviceStatut(String nom, int port, String baseUrl, String endpoint,
                                  String description, boolean enLigne, int nombreEnregistrements) {
            this.nom = nom;
            this.port = port;
            this.baseUrl = baseUrl;
            this.endpoint = endpoint;
            this.description = description;
            this.enLigne = enLigne;
            this.nombreEnregistrements = nombreEnregistrements;
        }

        public String getNom() {
            return nom;
        }

        public int getPort() {
            return port;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public String getDescription() {
            return description;
        }

        public boolean isEnLigne() {
            return enLigne;
        }

        public int getNombreEnregistrements() {
            return nombreEnregistrements;
        }
    }

    public List<MicroserviceStatut> verifierEtatMicroservices() {
        List<MicroserviceStatut> statuts = new ArrayList<>();
        statuts.add(testerMicroservice("ms-patrimoine", 8081, patrimoineUrl, "/api/patrimoines",
                "Fiches descriptives, photos, horaires d'ouverture, géolocalisation et avis"));
        statuts.add(testerMicroservice("ms-circuit", 8082, circuitUrl, "/api/circuits",
                "Itinéraires de visite optimisés (étapes, durée, distance, difficulté)"));
        statuts.add(testerMicroservice("ms-evenement", 8083, evenementUrl, "/api/evenements",
                "Planning des visites guidées et événements, capacité d'accueil et animations"));
        statuts.add(testerMicroservice("ms-reservation", 8084, reservationUrl, "/api/reservations",
                "Cycle de vie des réservations avec vérification de disponibilité"));
        statuts.add(testerMicroservice("ms-utilisateur", 8085, utilisateurUrl, "/api/utilisateurs",
                "Inscription, connexion, profils et rôles des utilisateurs"));
        statuts.add(testerMicroservice("ms-paiement", 8086, paiementUrl, "/api/paiements",
                "Transactions financières pour circuits et événements payants"));
        statuts.add(testerMicroservice("ms-notification", 8087, notificationUrl, "/api/notifications",
                "Notifications Email/SMS de confirmation, rappel et alerte"));
        return statuts;
    }

    private MicroserviceStatut testerMicroservice(String nom, int port, String baseUrl, String endpoint, String description) {
        try {
            ResponseEntity<List<Object>> response = restTemplate.exchange(
                    baseUrl + endpoint,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            int count = response.getBody() != null ? response.getBody().size() : 0;
            return new MicroserviceStatut(nom, port, baseUrl, endpoint, description, true, count);
        } catch (Exception e) {
            return new MicroserviceStatut(nom, port, baseUrl, endpoint, description, false, 0);
        }
    }

    public void synchroniserPatrimoine(SiteTouristique site) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("nom", site.getNom());
            payload.put("code", "PMZ-" + (site.getId() != null ? site.getId() : "WEB"));
            payload.put("description", site.getDescription());
            payload.put("adresse", site.getAdresse());
            payload.put("quartier", site.getQuartier());
            payload.put("imageUrl", site.getImageUrl());
            payload.put("latitude", site.getLatitude());
            payload.put("longitude", site.getLongitude());
            payload.put("capacite", 50);
            if (site.getCategorie() != null && site.getCategorie().getId() != null) {
                payload.put("categorie", Map.of("id", site.getCategorie().getId()));
            }
            restTemplate.postForEntity(patrimoineUrl + "/api/patrimoines", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-patrimoine hors ligne lors de la synchronisation : {}", e.getMessage());
        }
    }

    public void supprimerPatrimoine(Long id) {
        try {
            restTemplate.delete(patrimoineUrl + "/api/patrimoines/" + id);
        } catch (Exception e) {
            log.debug("ms-patrimoine hors ligne lors de la suppression : {}", e.getMessage());
        }
    }

    public void synchroniserAvis(Long siteId, Avis avis) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("auteur", avis.getAuteur());
            payload.put("commentaire", avis.getCommentaire());
            payload.put("note", avis.getNote());
            restTemplate.postForEntity(patrimoineUrl + "/api/patrimoines/" + siteId + "/avis", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-patrimoine hors ligne lors de l'ajout d'avis : {}", e.getMessage());
        }
    }

    public void synchroniserCategorie(Categorie categorie) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("nom", categorie.getNom());
            payload.put("description", categorie.getDescription());
            restTemplate.postForEntity(patrimoineUrl + "/api/categories", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-patrimoine hors ligne lors de la synchro catégorie : {}", e.getMessage());
        }
    }

    public void supprimerCategorie(Long id) {
        try {
            restTemplate.delete(patrimoineUrl + "/api/categories/" + id);
        } catch (Exception e) {
            log.debug("ms-patrimoine hors ligne lors de la suppression catégorie : {}", e.getMessage());
        }
    }

    public void synchroniserCircuit(Circuit circuit) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("nom", circuit.getNom());
            payload.put("description", circuit.getDescription());
            payload.put("dureeJours", circuit.getDureeJours());
            payload.put("prix", circuit.getPrix());
            payload.put("statut", circuit.getStatut() != null ? circuit.getStatut().name() : "BROUILLON");
            if (circuit.getOperateur() != null) {
                payload.put("operateurId", circuit.getOperateur().getId());
                payload.put("operateurNom", circuit.getOperateur().getNom());
            }
            restTemplate.postForEntity(circuitUrl + "/api/circuits", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-circuit hors ligne lors de la synchronisation : {}", e.getMessage());
        }
    }

    public void publierCircuit(Long id) {
        try {
            restTemplate.put(circuitUrl + "/api/circuits/" + id + "/publier", null);
        } catch (Exception e) {
            log.debug("ms-circuit hors ligne lors de la publication : {}", e.getMessage());
        }
    }

    public void supprimerCircuit(Long id) {
        try {
            restTemplate.delete(circuitUrl + "/api/circuits/" + id);
        } catch (Exception e) {
            log.debug("ms-circuit hors ligne lors de la suppression : {}", e.getMessage());
        }
    }

    public void synchroniserEvenement(Visite visite) {
        try {
            Map<String, Object> payload = new HashMap<>();
            String titre = visite.getSite() != null ? "Visite guidée : " + visite.getSite().getNom() : "Visite guidée";
            payload.put("titre", titre);
            payload.put("typeEvenement", "VISITE_GUIDEE");
            payload.put("guide", visite.getGuide());
            payload.put("dateHeureDebut", visite.getDateHeureDebut());
            payload.put("dureeMinutes", visite.getDureeMinutes());
            payload.put("capaciteMax", visite.getPlacesMax());
            if (visite.getSite() != null) {
                payload.put("patrimoineId", visite.getSite().getId());
                payload.put("patrimoineNom", visite.getSite().getNom());
                payload.put("lieuPrecis", visite.getSite().getAdresse());
            }
            if (visite.getCircuit() != null) {
                payload.put("circuitId", visite.getCircuit().getId());
                payload.put("circuitNom", visite.getCircuit().getNom());
                payload.put("prix", visite.getCircuit().getPrix());
            }
            restTemplate.postForEntity(evenementUrl + "/api/evenements", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-evenement hors ligne lors de la synchronisation : {}", e.getMessage());
        }
    }

    public void supprimerEvenement(Long id) {
        try {
            restTemplate.delete(evenementUrl + "/api/evenements/" + id);
        } catch (Exception e) {
            log.debug("ms-evenement hors ligne lors de la suppression : {}", e.getMessage());
        }
    }

    public void synchroniserReservation(Reservation reservation, Visite visite, User touriste) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("utilisateurId", touriste.getId());
            payload.put("utilisateurNom", reservation.getNomReservant() != null ? reservation.getNomReservant() : touriste.getNom());
            payload.put("utilisateurEmail", reservation.getEmailReservant() != null ? reservation.getEmailReservant() : touriste.getEmail());
            payload.put("typeReservation", "EVENEMENT");
            payload.put("evenementId", visite.getId());
            if (visite.getCircuit() != null) {
                payload.put("circuitId", visite.getCircuit().getId());
            }
            payload.put("nomCible", visite.getSite() != null ? visite.getSite().getNom() : "Visite guidée");
            payload.put("dateHeureCible", visite.getDateHeureDebut());
            payload.put("nombrePlaces", reservation.getNombrePlaces() != null ? reservation.getNombrePlaces() : 1);
            payload.put("montant", reservation.getMontant());
            payload.put("qrCodeHash", reservation.getQrCodeHash());
            restTemplate.postForEntity(reservationUrl + "/api/reservations", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-reservation hors ligne lors de la synchronisation : {}", e.getMessage());
        }
    }

    public void annulerReservation(Long id) {
        try {
            restTemplate.put(reservationUrl + "/api/reservations/" + id + "/annuler", null);
        } catch (Exception e) {
            log.debug("ms-reservation hors ligne lors de l'annulation : {}", e.getMessage());
        }
    }

    public void supprimerReservation(Long id) {
        try {
            restTemplate.delete(reservationUrl + "/api/reservations/" + id);
        } catch (Exception e) {
            log.debug("ms-reservation hors ligne lors de la suppression : {}", e.getMessage());
        }
    }

    public void synchroniserUtilisateur(User user, String motDePasseBrut) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("nom", user.getNom());
            payload.put("email", user.getEmail());
            payload.put("motDePasse", motDePasseBrut);
            payload.put("role", user.getRole() != null ? user.getRole().name() : "VISITEUR");
            restTemplate.postForEntity(utilisateurUrl + "/api/utilisateurs/inscription", payload, Object.class);
        } catch (Exception e) {
            log.debug("ms-utilisateur hors ligne lors de l'inscription : {}", e.getMessage());
        }
    }
}

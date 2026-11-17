package com.uasz.ms_circuit.config;

import com.uasz.ms_circuit.entities.Circuit;
import com.uasz.ms_circuit.entities.EtapeCircuit;
import com.uasz.ms_circuit.entities.StatutCircuit;
import com.uasz.ms_circuit.repositories.CircuitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final CircuitRepository circuitRepository;

    public DonneesInitiales(CircuitRepository circuitRepository) {
        this.circuitRepository = circuitRepository;
    }

    @Override
    public void run(String... args) {
        if (circuitRepository.count() > 0) {
            return;
        }

        Circuit circuitCulturel = new Circuit();
        circuitCulturel.setNom("Découverte culturelle de Ziguinchor");
        circuitCulturel.setDescription("Un circuit d'une journée pour découvrir le patrimoine culturel, mémoriel et religieux du centre-ville.");
        circuitCulturel.setDureeJours(1);
        circuitCulturel.setDureeEstimee("4 heures");
        circuitCulturel.setDistanceKm(4.5);
        circuitCulturel.setDifficulte("Facile");
        circuitCulturel.setPrix(15000.0);
        circuitCulturel.setStatut(StatutCircuit.PUBLIE);
        circuitCulturel.setCapaciteMax(25);
        circuitCulturel.setPlacesReservees(2);
        circuitCulturel.setOperateurId(2L);
        circuitCulturel.setOperateurNom("Opérateur Touristique Test");
        circuitCulturel.setPatrimoineIds(List.of(1L, 2L, 3L));

        EtapeCircuit etape1 = new EtapeCircuit(null, 1, "Mémorial-Musée Le Joola",
                "Accueil et visite du mémorial au quartier Escale.", 60, 1L, circuitCulturel);
        EtapeCircuit etape2 = new EtapeCircuit(null, 2, "Cathédrale Saint Antoine",
                "Découverte de l'architecture coloniale au centre-ville.", 90, 2L, circuitCulturel);
        EtapeCircuit etape3 = new EtapeCircuit(null, 3, "Marché Saint Maur",
                "Immersion artisanale et dégustation de produits locaux.", 90, 3L, circuitCulturel);
        circuitCulturel.setEtapes(List.of(etape1, etape2, etape3));
        circuitRepository.save(circuitCulturel);

        Circuit circuitNature = new Circuit();
        circuitNature.setNom("Balade nature le long du fleuve Casamance");
        circuitNature.setDescription("Circuit d'une demi-journée pour explorer la mangrove et le fleuve en pirogue.");
        circuitNature.setDureeJours(1);
        circuitNature.setDureeEstimee("3 heures");
        circuitNature.setDistanceKm(8.2);
        circuitNature.setDifficulte("Modérée");
        circuitNature.setPrix(10000.0);
        circuitNature.setStatut(StatutCircuit.PUBLIE);
        circuitNature.setCapaciteMax(15);
        circuitNature.setPlacesReservees(0);
        circuitNature.setOperateurId(2L);
        circuitNature.setOperateurNom("Opérateur Touristique Test");
        circuitNature.setPatrimoineIds(List.of(4L, 5L));

        EtapeCircuit etapeNature1 = new EtapeCircuit(null, 1, "Embarcadère de Boucotte",
                "Navigation en pirogue traditionnelle sur le fleuve Casamance.", 120, 4L, circuitNature);
        EtapeCircuit etapeNature2 = new EtapeCircuit(null, 2, "Cases à étage de Boucotte",
                "Observation de l'architecture coloniale et traditionnelle.", 60, 5L, circuitNature);
        circuitNature.setEtapes(List.of(etapeNature1, etapeNature2));
        circuitRepository.save(circuitNature);

        Circuit circuitBrouillon = new Circuit();
        circuitBrouillon.setNom("Circuit artisanat (en préparation)");
        circuitBrouillon.setDescription("Circuit encore en cours de conception, non publié.");
        circuitBrouillon.setDureeJours(1);
        circuitBrouillon.setDureeEstimee("2 heures 30");
        circuitBrouillon.setDistanceKm(3.0);
        circuitBrouillon.setDifficulte("Facile");
        circuitBrouillon.setPrix(8000.0);
        circuitBrouillon.setStatut(StatutCircuit.BROUILLON);
        circuitBrouillon.setCapaciteMax(20);
        circuitBrouillon.setPlacesReservees(0);
        circuitBrouillon.setOperateurId(2L);
        circuitBrouillon.setOperateurNom("Opérateur Touristique Test");
        circuitBrouillon.setPatrimoineIds(List.of(3L, 6L));
        circuitRepository.save(circuitBrouillon);
    }
}

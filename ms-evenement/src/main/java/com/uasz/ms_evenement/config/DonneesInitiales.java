package com.uasz.ms_evenement.config;

import com.uasz.ms_evenement.entities.Evenement;
import com.uasz.ms_evenement.entities.StatutEvenement;
import com.uasz.ms_evenement.repositories.EvenementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final EvenementRepository evenementRepository;

    public DonneesInitiales(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    @Override
    public void run(String... args) {
        if (evenementRepository.count() > 0) {
            return;
        }

        Evenement visite1 = new Evenement();
        visite1.setTitre("Visite guidée : Cathédrale Saint Antoine");
        visite1.setTypeEvenement("VISITE_GUIDEE");
        visite1.setDescription("Découverte architecturale et historique de la Cathédrale Saint Antoine de Ziguinchor.");
        visite1.setLieuPrecis("Parvis de la Cathédrale, Avenue du Général de Gaulle");
        visite1.setPatrimoineId(2L);
        visite1.setPatrimoineNom("Cathédrale Saint Antoine de Ziguinchor");
        visite1.setCircuitId(1L);
        visite1.setCircuitNom("Découverte culturelle de Ziguinchor");
        visite1.setGuide("Ibrahima Sané");
        visite1.setDateHeureDebut(LocalDateTime.now().plusDays(3).withHour(9).withMinute(0));
        visite1.setDureeMinutes(90);
        visite1.setCapaciteMax(15);
        visite1.setPlacesReservees(1);
        visite1.setPrix(15000.0);
        visite1.setStatut(StatutEvenement.PLANIFIEE);
        evenementRepository.save(visite1);

        Evenement visite2 = new Evenement();
        visite2.setTitre("Immersion au Marché Saint Maur");
        visite2.setTypeEvenement("VISITE_GUIDEE");
        visite2.setDescription("Parcours guidé parmi les artisans et étals traditionnels du Marché Saint Maur.");
        visite2.setLieuPrecis("Entrée principale du Marché Saint Maur, Escale");
        visite2.setPatrimoineId(3L);
        visite2.setPatrimoineNom("Marché Saint Maur");
        visite2.setCircuitId(1L);
        visite2.setCircuitNom("Découverte culturelle de Ziguinchor");
        visite2.setGuide("Ibrahima Sané");
        visite2.setDateHeureDebut(LocalDateTime.now().plusDays(3).withHour(11).withMinute(0));
        visite2.setDureeMinutes(60);
        visite2.setCapaciteMax(15);
        visite2.setPlacesReservees(0);
        visite2.setPrix(15000.0);
        visite2.setStatut(StatutEvenement.PLANIFIEE);
        evenementRepository.save(visite2);

        Evenement visite3 = new Evenement();
        visite3.setTitre("Balade en pirogue sur le Fleuve Casamance");
        visite3.setTypeEvenement("VISITE_GUIDEE");
        visite3.setDescription("Observation de la mangrove et découverte des berges du fleuve Casamance.");
        visite3.setLieuPrecis("Embarcadère de Boucotte");
        visite3.setPatrimoineId(4L);
        visite3.setPatrimoineNom("Le Fleuve Casamance (rive de Ziguinchor)");
        visite3.setCircuitId(2L);
        visite3.setCircuitNom("Balade nature le long du fleuve Casamance");
        visite3.setGuide("Fatou Diémé");
        visite3.setDateHeureDebut(LocalDateTime.now().plusDays(5).withHour(8).withMinute(30));
        visite3.setDureeMinutes(120);
        visite3.setCapaciteMax(10);
        visite3.setPlacesReservees(0);
        visite3.setPrix(10000.0);
        visite3.setStatut(StatutEvenement.PLANIFIEE);
        evenementRepository.save(visite3);

        Evenement expo = new Evenement();
        expo.setTitre("Exposition d'art contemporain de Casamance");
        expo.setTypeEvenement("EXPOSITION");
        expo.setDescription("Exposition temporaire mettant en valeur les peintres, sculpteurs et tisserands casamançais.");
        expo.setLieuPrecis("Grande salle de l'Alliance Franco-Sénégalaise, Escale");
        expo.setPatrimoineId(6L);
        expo.setPatrimoineNom("Alliance Franco-Sénégalaise de Ziguinchor");
        expo.setGuide("Aminata Badji");
        expo.setDateHeureDebut(LocalDateTime.now().plusDays(7).withHour(15).withMinute(0));
        expo.setDureeMinutes(150);
        expo.setCapaciteMax(40);
        expo.setPlacesReservees(4);
        expo.setPrix(3000.0);
        expo.setStatut(StatutEvenement.PLANIFIEE);
        evenementRepository.save(expo);

        Evenement visitePassee = new Evenement();
        visitePassee.setTitre("Visite historique : Cathédrale Saint Antoine (session passée)");
        visitePassee.setTypeEvenement("VISITE_GUIDEE");
        visitePassee.setDescription("Visite guidée déjà réalisée.");
        visitePassee.setLieuPrecis("Centre-ville de Ziguinchor");
        visitePassee.setPatrimoineId(2L);
        visitePassee.setPatrimoineNom("Cathédrale Saint Antoine de Ziguinchor");
        visitePassee.setGuide("Ibrahima Sané");
        visitePassee.setDateHeureDebut(LocalDateTime.now().minusDays(2).withHour(10).withMinute(0));
        visitePassee.setDureeMinutes(90);
        visitePassee.setCapaciteMax(15);
        visitePassee.setPlacesReservees(1);
        visitePassee.setPrix(5000.0);
        visitePassee.setStatut(StatutEvenement.TERMINEE);
        evenementRepository.save(visitePassee);
    }
}

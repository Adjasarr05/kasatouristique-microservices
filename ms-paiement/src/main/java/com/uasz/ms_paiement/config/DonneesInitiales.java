package com.uasz.ms_paiement.config;

import com.uasz.ms_paiement.entities.Paiement;
import com.uasz.ms_paiement.entities.StatutPaiement;
import com.uasz.ms_paiement.repositories.PaiementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final PaiementRepository paiementRepository;

    public DonneesInitiales(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    @Override
    public void run(String... args) {
        if (paiementRepository.count() > 0) {
            return;
        }

        Paiement p1 = new Paiement();
        p1.setReservationId(1L);
        p1.setUtilisateurId(5L);
        p1.setMontant(15000.0);
        p1.setModePaiement("WAVE");
        p1.setStatut(StatutPaiement.VALIDE);
        paiementRepository.save(p1);

        Paiement p2 = new Paiement();
        p2.setReservationId(2L);
        p2.setUtilisateurId(5L);
        p2.setMontant(5000.0);
        p2.setModePaiement("ORANGE_MONEY");
        p2.setStatut(StatutPaiement.VALIDE);
        paiementRepository.save(p2);
    }
}

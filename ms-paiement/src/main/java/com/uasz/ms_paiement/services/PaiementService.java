package com.uasz.ms_paiement.services;

import com.uasz.ms_paiement.entities.Paiement;
import com.uasz.ms_paiement.entities.StatutPaiement;
import com.uasz.ms_paiement.repositories.PaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaiementService implements IPaiementService {

    private final PaiementRepository paiementRepository;

    public PaiementService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    @Override
    public Paiement savePaiement(Paiement paiement) {
        if (paiement.getMontant() == null || paiement.getMontant() < 0) {
            throw new IllegalArgumentException("Le montant du paiement doit être positif.");
        }
        if (paiement.getStatut() == null) {
            paiement.setStatut(StatutPaiement.VALIDE);
        }
        return paiementRepository.save(paiement);
    }

    @Override
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    @Override
    public List<Paiement> getByReservationId(Long reservationId) {
        return paiementRepository.findByReservationId(reservationId);
    }

    @Override
    public List<Paiement> getByUtilisateurId(Long utilisateurId) {
        return paiementRepository.findByUtilisateurId(utilisateurId);
    }

    @Override
    public Optional<Paiement> getByIdPaiement(Long id) {
        return paiementRepository.findById(id);
    }

    @Override
    public Paiement rembourserPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id : " + id));
        paiement.setStatut(StatutPaiement.REMBOURSE);
        return paiementRepository.save(paiement);
    }

    @Override
    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }
}

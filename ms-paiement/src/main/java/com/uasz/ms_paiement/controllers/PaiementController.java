package com.uasz.ms_paiement.controllers;

import com.uasz.ms_paiement.entities.Paiement;
import com.uasz.ms_paiement.services.PaiementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
@CrossOrigin("*")
public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @PostMapping
    public ResponseEntity<?> createPaiement(@RequestBody Paiement paiement) {
        try {
            return ResponseEntity.ok(paiementService.savePaiement(paiement));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }

    @GetMapping
    public List<Paiement> getAllPaiements(@RequestParam(required = false) Long reservationId,
                                          @RequestParam(required = false) Long utilisateurId) {
        if (reservationId != null) {
            return paiementService.getByReservationId(reservationId);
        }
        if (utilisateurId != null) {
            return paiementService.getByUtilisateurId(utilisateurId);
        }
        return paiementService.getAllPaiements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        return paiementService.getByIdPaiement(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/rembourser")
    public ResponseEntity<Paiement> rembourserPaiement(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(paiementService.rembourserPaiement(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        paiementService.deletePaiement(id);
        return ResponseEntity.noContent().build();
    }
}

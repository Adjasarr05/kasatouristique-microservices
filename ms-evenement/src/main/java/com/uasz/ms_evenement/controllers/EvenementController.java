package com.uasz.ms_evenement.controllers;

import com.uasz.ms_evenement.entities.Evenement;
import com.uasz.ms_evenement.services.EvenementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evenements")
@CrossOrigin("*")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    @PostMapping
    public Evenement createEvenement(@RequestBody Evenement evenement) {
        return evenementService.saveEvenement(evenement);
    }

    @GetMapping
    public List<Evenement> getAllEvenements(@RequestParam(required = false) Boolean aVenir,
                                            @RequestParam(required = false) Long patrimoineId,
                                            @RequestParam(required = false) Long circuitId) {
        if (Boolean.TRUE.equals(aVenir)) {
            return evenementService.getEvenementsAVenir();
        }
        if (patrimoineId != null) {
            return evenementService.getByPatrimoineId(patrimoineId);
        }
        if (circuitId != null) {
            return evenementService.getByCircuitId(circuitId);
        }
        return evenementService.getAllEvenements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getEvenementById(@PathVariable Long id) {
        return evenementService.getByIdEvenement(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evenement> updateEvenement(@PathVariable Long id, @RequestBody Evenement evenement) {
        try {
            return ResponseEntity.ok(evenementService.updateEvenement(id, evenement));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.deleteEvenement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/disponibilite")
    public ResponseEntity<Map<String, Object>> verifierDisponibilite(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(evenementService.verifierDisponibilite(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reserver-place")
    public ResponseEntity<Evenement> reserverPlace(@PathVariable Long id,
                                                   @RequestParam(defaultValue = "1") int quantite) {
        try {
            return ResponseEntity.ok(evenementService.reserverPlace(id, quantite));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/liberer-place")
    public ResponseEntity<Evenement> libererPlace(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "1") int quantite) {
        try {
            return ResponseEntity.ok(evenementService.libererPlace(id, quantite));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.uasz.ms_patrimoine.controllers;

import com.uasz.ms_patrimoine.entities.Avis;
import com.uasz.ms_patrimoine.entities.Patrimoine;
import com.uasz.ms_patrimoine.services.PatrimoineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrimoines")
@CrossOrigin("*")
public class PatrimoineController {

    private final PatrimoineService patrimoineService;

    public PatrimoineController(PatrimoineService patrimoineService) {
        this.patrimoineService = patrimoineService;
    }

    @PostMapping
    public Patrimoine createPatrimoine(@RequestBody Patrimoine patrimoine) {
        return this.patrimoineService.savePatrimoine(patrimoine);
    }

    @GetMapping
    public List<Patrimoine> getAllPatrimoines(@RequestParam(required = false) String recherche,
                                              @RequestParam(required = false) Long categorieId) {
        if (recherche != null && !recherche.isBlank()) {
            return this.patrimoineService.rechercherParNom(recherche);
        }
        if (categorieId != null) {
            return this.patrimoineService.getByCategorie(categorieId);
        }
        return this.patrimoineService.getAllPatrimoines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patrimoine> getPatrimoineById(@PathVariable Long id) {
        return this.patrimoineService.getByIdPatrimoine(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patrimoine> updatePatrimoine(@PathVariable Long id,
                                                     @RequestBody Patrimoine patrimoine) {
        try {
            Patrimoine updated = this.patrimoineService.updatePatrimoine(id, patrimoine);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatrimoine(@PathVariable Long id) {
        this.patrimoineService.deletePatrimoine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/avis")
    public ResponseEntity<Avis> addAvis(@PathVariable Long id, @RequestBody Avis avis) {
        try {
            return ResponseEntity.ok(this.patrimoineService.ajouterAvis(id, avis));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

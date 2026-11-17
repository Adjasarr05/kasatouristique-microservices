package com.uasz.ms_circuit.controllers;

import com.uasz.ms_circuit.entities.Circuit;
import com.uasz.ms_circuit.entities.StatutCircuit;
import com.uasz.ms_circuit.services.CircuitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/circuits")
@CrossOrigin("*")
public class CircuitController {

    private final CircuitService circuitService;

    public CircuitController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    @PostMapping
    public Circuit createCircuit(@RequestBody Circuit circuit) {
        return circuitService.saveCircuit(circuit);
    }

    @GetMapping
    public List<Circuit> getAllCircuits(@RequestParam(required = false) StatutCircuit statut,
                                        @RequestParam(required = false) Long operateurId) {
        if (statut == StatutCircuit.PUBLIE) {
            return circuitService.getCircuitsPublies();
        }
        if (operateurId != null) {
            return circuitService.getByOperateurId(operateurId);
        }
        return circuitService.getAllCircuits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Circuit> getCircuitById(@PathVariable Long id) {
        return circuitService.getByIdCircuit(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Circuit> updateCircuit(@PathVariable Long id, @RequestBody Circuit circuit) {
        try {
            return ResponseEntity.ok(circuitService.updateCircuit(id, circuit));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuit(@PathVariable Long id) {
        circuitService.deleteCircuit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/publier")
    public ResponseEntity<Circuit> publierCircuit(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(circuitService.publierCircuit(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/disponibilite")
    public ResponseEntity<Map<String, Object>> verifierDisponibilite(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(circuitService.verifierDisponibilite(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reserver-place")
    public ResponseEntity<Circuit> reserverPlace(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "1") int quantite) {
        try {
            return ResponseEntity.ok(circuitService.reserverPlace(id, quantite));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/liberer-place")
    public ResponseEntity<Circuit> libererPlace(@PathVariable Long id,
                                                @RequestParam(defaultValue = "1") int quantite) {
        try {
            return ResponseEntity.ok(circuitService.libererPlace(id, quantite));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Circuit;
import com.ziguinchor.patrimoine.model.StatutCircuit;
import com.ziguinchor.patrimoine.repository.CircuitRepository;
import com.ziguinchor.patrimoine.security.CustomUserDetails;
import com.ziguinchor.patrimoine.service.MicroserviceGatewayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/circuits")
public class CircuitController {

    private final CircuitRepository circuitRepository;
    private final MicroserviceGatewayService microserviceGatewayService;

    @Autowired
    public CircuitController(CircuitRepository circuitRepository,
                             MicroserviceGatewayService microserviceGatewayService) {
        this.circuitRepository = circuitRepository;
        this.microserviceGatewayService = microserviceGatewayService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("circuits", circuitRepository.findByStatut(StatutCircuit.PUBLIE));
        return "circuits/liste";
    }

    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("circuit", new Circuit());
        return "circuits/form";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("circuit") Circuit circuit,
                               BindingResult resultat,
                               @AuthenticationPrincipal CustomUserDetails principal) {
        if (resultat.hasErrors()) return "circuits/form";
        circuit.setOperateur(principal.getUser());
        Circuit saved = circuitRepository.save(circuit);
        microserviceGatewayService.synchroniserCircuit(saved);
        return "redirect:/circuits";
    }

    @PostMapping("/publier/{id}")
    public String publier(@PathVariable Long id) {
        Circuit circuit = circuitRepository.findById(id).orElseThrow();
        circuit.setStatut(StatutCircuit.PUBLIE);
        circuitRepository.save(circuit);
        microserviceGatewayService.publierCircuit(id);
        return "redirect:/circuits/mes-circuits";
    }

    @GetMapping("/mes-circuits")
    public String mesCircuits(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        model.addAttribute("circuits", circuitRepository.findByOperateur(principal.getUser()));
        return "circuits/mes-circuits";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        circuitRepository.deleteById(id);
        microserviceGatewayService.supprimerCircuit(id);
        return "redirect:/circuits/mes-circuits";
    }
}

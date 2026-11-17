package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Circuit;
import com.ziguinchor.patrimoine.model.StatutCircuit;
import com.ziguinchor.patrimoine.model.Visite;
import com.ziguinchor.patrimoine.repository.CircuitRepository;
import com.ziguinchor.patrimoine.repository.SiteTouristiqueRepository;
import com.ziguinchor.patrimoine.repository.VisiteRepository;
import com.ziguinchor.patrimoine.service.MicroserviceGatewayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/visites")
public class VisiteController {

    private final VisiteRepository visiteRepository;
    private final SiteTouristiqueRepository siteRepository;
    private final CircuitRepository circuitRepository;
    private final MicroserviceGatewayService microserviceGatewayService;

    @Autowired
    public VisiteController(VisiteRepository visiteRepository,
                            SiteTouristiqueRepository siteRepository,
                            CircuitRepository circuitRepository,
                            MicroserviceGatewayService microserviceGatewayService) {
        this.visiteRepository = visiteRepository;
        this.siteRepository = siteRepository;
        this.circuitRepository = circuitRepository;
        this.microserviceGatewayService = microserviceGatewayService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("visites", visiteRepository.findByDateHeureDebutAfterOrderByDateHeureDebutAsc(LocalDateTime.now()));
        return "visites/liste";
    }

    @GetMapping("/planifier")
    public String formulairePlanification(Model model) {
        model.addAttribute("visite", new Visite());
        model.addAttribute("sites", siteRepository.findAll());
        List<Circuit> circuits = circuitRepository.findByStatut(StatutCircuit.PUBLIE);
        model.addAttribute("circuits", circuits);
        return "visites/form";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("visite") Visite visite,
                              BindingResult resultat,
                              Model model) {
        if (visite.getDateHeureDebut() != null && visite.getDateHeureDebut().isBefore(LocalDateTime.now())) {
            resultat.rejectValue("dateHeureDebut", "error.visite", "La visite doit être planifiée dans le futur");
        }
        if (resultat.hasErrors()) {
            model.addAttribute("sites", siteRepository.findAll());
            model.addAttribute("circuits", circuitRepository.findByStatut(StatutCircuit.PUBLIE));
            return "visites/form";
        }
        Visite saved = visiteRepository.save(visite);
        microserviceGatewayService.synchroniserEvenement(saved);
        return "redirect:/visites";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        visiteRepository.deleteById(id);
        microserviceGatewayService.supprimerEvenement(id);
        return "redirect:/visites";
    }
}
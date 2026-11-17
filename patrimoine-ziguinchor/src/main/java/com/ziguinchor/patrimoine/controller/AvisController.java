package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Avis;
import com.ziguinchor.patrimoine.repository.AvisRepository;
import com.ziguinchor.patrimoine.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/avis")
public class AvisController {

    private final AvisRepository avisRepository;

    @Autowired
    public AvisController(AvisRepository avisRepository) {
        this.avisRepository = avisRepository;
    }

    @PostMapping("/{id}/repondre")
    public String repondre(@PathVariable Long id,
                           @RequestParam String reponseGuide,
                           @RequestParam(defaultValue = "site") String provenance,
                           @AuthenticationPrincipal CustomUserDetails principal,
                           RedirectAttributes redirectAttributes) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avis introuvable"));
        avis.setReponseGuide(reponseGuide != null ? reponseGuide.trim() : "");
        avis.setAuteurReponse(principal != null ? principal.getUser().getNom() : "Guide touristique");
        avis.setDateReponse(LocalDate.now());
        avisRepository.save(avis);

        redirectAttributes.addFlashAttribute("messageSucces", "Réponse publiée avec succès.");
        if ("guide".equals(provenance)) {
            return "redirect:/guide/avis";
        }
        Long siteId = avis.getSiteTouristique() != null ? avis.getSiteTouristique().getId() : null;
        return siteId != null ? "redirect:/sites/" + siteId : "redirect:/sites";
    }

    @PostMapping("/{id}/signaler")
    public String signaler(@PathVariable Long id,
                           @RequestParam(defaultValue = "Contenu inapproprié ou abusif") String motif,
                           @RequestParam(defaultValue = "site") String provenance,
                           RedirectAttributes redirectAttributes) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avis introuvable"));
        avis.setSignale(true);
        avis.setMotifSignalement(motif != null && !motif.isBlank() ? motif.trim() : "Avis signalé pour modération");
        avisRepository.save(avis);

        redirectAttributes.addFlashAttribute("messageSucces", "L'avis a été signalé à l'administration pour modération.");
        if ("guide".equals(provenance)) {
            return "redirect:/guide/avis";
        }
        Long siteId = avis.getSiteTouristique() != null ? avis.getSiteTouristique().getId() : null;
        return siteId != null ? "redirect:/sites/" + siteId : "redirect:/sites";
    }
}

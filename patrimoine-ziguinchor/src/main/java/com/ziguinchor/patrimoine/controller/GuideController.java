package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Avis;
import com.ziguinchor.patrimoine.model.User;
import com.ziguinchor.patrimoine.model.Visite;
import com.ziguinchor.patrimoine.repository.AvisRepository;
import com.ziguinchor.patrimoine.repository.UserRepository;
import com.ziguinchor.patrimoine.repository.VisiteRepository;
import com.ziguinchor.patrimoine.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/guide")
public class GuideController {

    private final UserRepository userRepository;
    private final VisiteRepository visiteRepository;
    private final AvisRepository avisRepository;

    @Autowired
    public GuideController(UserRepository userRepository,
                           VisiteRepository visiteRepository,
                           AvisRepository avisRepository) {
        this.userRepository = userRepository;
        this.visiteRepository = visiteRepository;
        this.avisRepository = avisRepository;
    }

    @GetMapping("/agenda")
    public String agenda(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        User guide = userRepository.findById(principal.getUser().getId()).orElse(principal.getUser());
        LocalDate aujourdhui = LocalDate.now();
        LocalDateTime debutJour = aujourdhui.atStartOfDay();
        LocalDateTime finJour = aujourdhui.atTime(LocalTime.MAX);

        List<Visite> visitesDuJour = visiteRepository
                .findByGuideContainingIgnoreCaseAndDateHeureDebutBetweenOrderByDateHeureDebutAsc(
                        guide.getNom(), debutJour, finJour);
        List<Visite> toutesMesVisites = visiteRepository
                .findByGuideContainingIgnoreCaseOrderByDateHeureDebutAsc(guide.getNom());

        if (visitesDuJour.isEmpty()) {
            visitesDuJour = visiteRepository.findByDateHeureDebutBetweenOrderByDateHeureDebutAsc(debutJour, finJour);
        }

        model.addAttribute("guide", guide);
        model.addAttribute("dateAujourdhui", aujourdhui);
        model.addAttribute("visitesDuJour", visitesDuJour);
        model.addAttribute("toutesMesVisites", toutesMesVisites);
        return "guide/agenda";
    }

    @GetMapping("/profil")
    public String profil(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        User guide = userRepository.findById(principal.getUser().getId()).orElse(principal.getUser());
        model.addAttribute("guide", guide);
        return "guide/profil";
    }

    @PostMapping("/profil")
    public String enregistrerProfil(@AuthenticationPrincipal CustomUserDetails principal,
                                    @RequestParam String nom,
                                    @RequestParam(required = false) String telephone,
                                    @RequestParam(required = false) String specialite,
                                    @RequestParam(required = false) String languesParlees,
                                    @RequestParam(required = false) String zoneIntervention,
                                    @RequestParam(required = false) Integer anneesExperience,
                                    @RequestParam(required = false) String biographie,
                                    @RequestParam(defaultValue = "false") boolean disponible,
                                    RedirectAttributes redirectAttributes) {
        User guide = userRepository.findById(principal.getUser().getId()).orElseThrow();
        if (nom != null && !nom.isBlank()) {
            guide.setNom(nom.trim());
        }
        guide.setTelephone(telephone != null ? telephone.trim() : null);
        guide.setSpecialite(specialite != null ? specialite.trim() : null);
        guide.setLanguesParlees(languesParlees != null ? languesParlees.trim() : null);
        guide.setZoneIntervention(zoneIntervention != null ? zoneIntervention.trim() : null);
        guide.setAnneesExperience(anneesExperience);
        guide.setBiographie(biographie != null ? biographie.trim() : null);
        guide.setDisponible(disponible);
        userRepository.save(guide);

        redirectAttributes.addFlashAttribute("messageSucces", "Profil professionnel mis à jour avec succès !");
        return "redirect:/guide/profil";
    }

    @GetMapping("/avis")
    public String avis(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        User guide = userRepository.findById(principal.getUser().getId()).orElse(principal.getUser());
        List<Avis> tousLesAvis = avisRepository.findAllByOrderByDateAvisDesc();
        model.addAttribute("guide", guide);
        model.addAttribute("avisList", tousLesAvis);
        return "guide/avis";
    }
}

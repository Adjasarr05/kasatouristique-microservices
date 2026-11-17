package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.*;
import com.ziguinchor.patrimoine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final SiteTouristiqueRepository siteRepository;
    private final CircuitRepository circuitRepository;
    private final VisiteRepository visiteRepository;
    private final ReservationRepository reservationRepository;
    private final AvisRepository avisRepository;

    @Autowired
    public AdminController(UserRepository userRepository,
                           SiteTouristiqueRepository siteRepository,
                           CircuitRepository circuitRepository,
                           VisiteRepository visiteRepository,
                           ReservationRepository reservationRepository,
                           AvisRepository avisRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.circuitRepository = circuitRepository;
        this.visiteRepository = visiteRepository;
        this.reservationRepository = reservationRepository;
        this.avisRepository = avisRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> guidesEnAttente = userRepository.findByRoleAndStatutValidation(Role.GUIDE, StatutValidation.EN_ATTENTE);
        List<User> guidesTous = userRepository.findByRole(Role.GUIDE);
        List<Avis> avisSignales = avisRepository.findBySignaleTrueOrderByDateAvisDesc();
        List<User> utilisateurs = userRepository.findAllByOrderByIdDesc();

        model.addAttribute("nbSites", siteRepository.count());
        model.addAttribute("nbCircuits", circuitRepository.count());
        model.addAttribute("nbVisites", visiteRepository.count());
        model.addAttribute("nbReservations", reservationRepository.count());
        model.addAttribute("nbUtilisateurs", userRepository.count());
        model.addAttribute("guidesEnAttente", guidesEnAttente);
        model.addAttribute("guidesTous", guidesTous);
        model.addAttribute("avisSignales", avisSignales);
        model.addAttribute("sites", siteRepository.findAll());
        model.addAttribute("circuits", circuitRepository.findAll());
        model.addAttribute("visites", visiteRepository.findAll());
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("roles", Role.values());
        return "admin/dashboard";
    }

    @PostMapping("/guides/{id}/valider")
    public String validerGuide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User guide = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Guide introuvable"));
        guide.setStatutValidation(StatutValidation.VALIDE);
        guide.setActif(true);
        userRepository.save(guide);
        redirectAttributes.addFlashAttribute("messageSucces", "Le compte guide de " + guide.getNom() + " a été validé.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/guides/{id}/refuser")
    public String refuserGuide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User guide = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Guide introuvable"));
        guide.setStatutValidation(StatutValidation.REFUSE);
        guide.setActif(false);
        userRepository.save(guide);
        redirectAttributes.addFlashAttribute("messageSucces", "La demande guide de " + guide.getNom() + " a été refusée.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/avis/{id}/ignorer-signalement")
    public String ignorerSignalement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Avis avis = avisRepository.findById(id).orElseThrow();
        avis.setSignale(false);
        avis.setMasque(false);
        avisRepository.save(avis);
        redirectAttributes.addFlashAttribute("messageSucces", "Signalement levé, avis conservé.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/avis/{id}/supprimer")
    public String supprimerAvisAbusif(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        avisRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("messageSucces", "Avis abusif supprimé avec succès.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/utilisateurs/{id}/toggle-actif")
    public String toggleActif(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActif(!user.isActif());
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("messageSucces",
                "Statut du compte " + user.getEmail() + " mis à jour.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/utilisateurs/{id}/role")
    public String changerRole(@PathVariable Long id,
                              @RequestParam Role role,
                              RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("messageSucces",
                "Rôle de " + user.getEmail() + " modifié en " + role.name() + ".");
        return "redirect:/admin/dashboard";
    }
}

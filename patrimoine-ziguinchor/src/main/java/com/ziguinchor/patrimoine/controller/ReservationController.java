package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Reservation;
import com.ziguinchor.patrimoine.model.StatutReservation;
import com.ziguinchor.patrimoine.model.User;
import com.ziguinchor.patrimoine.model.Visite;
import com.ziguinchor.patrimoine.repository.ReservationRepository;
import com.ziguinchor.patrimoine.repository.UserRepository;
import com.ziguinchor.patrimoine.repository.VisiteRepository;
import com.ziguinchor.patrimoine.security.CustomUserDetails;
import com.ziguinchor.patrimoine.service.MicroserviceGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private static final double TARIF_VISITE_STANDARD = 5000.0;

    private final ReservationRepository reservationRepository;
    private final VisiteRepository visiteRepository;
    private final UserRepository userRepository;
    private final MicroserviceGatewayService microserviceGatewayService;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository,
                                 VisiteRepository visiteRepository,
                                 UserRepository userRepository,
                                 MicroserviceGatewayService microserviceGatewayService) {
        this.reservationRepository = reservationRepository;
        this.visiteRepository = visiteRepository;
        this.userRepository = userRepository;
        this.microserviceGatewayService = microserviceGatewayService;
    }

    @GetMapping
    public String mesReservations(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        User touriste = principal.getUser();
        model.addAttribute("reservations", reservationRepository.findByTouristeOrderByDateReservationDesc(touriste));
        return "reservations/liste";
    }

    @GetMapping("/billet/{code}")
    public String afficherBillet(@PathVariable String code, Model model) {
        Reservation reservation = reservationRepository.findByQrCodeHash(code)
                .orElseGet(() -> {
                    try {
                        return reservationRepository.findById(Long.valueOf(code)).orElse(null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                });
        if (reservation == null) {
            return "redirect:/reservations";
        }
        model.addAttribute("reservation", reservation);
        return "reservations/billet";
    }

    @GetMapping("/nouvelle/{visiteId}")
    public String formulaireReservation(@PathVariable Long visiteId,
                                        @AuthenticationPrincipal CustomUserDetails principal,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        Visite visite = visiteRepository.findById(visiteId)
                .orElseThrow(() -> new IllegalArgumentException("Visite introuvable"));

        if (visite.isComplete()) {
            redirectAttributes.addFlashAttribute("messagePatience",
                    "Cette visite est complète. Merci de patienter : une place pourrait se libérer en cas d'annulation, ou choisissez une autre date.");
            return "redirect:/visites";
        }

        User touriste = userRepository.findById(principal.getUser().getId()).orElse(principal.getUser());
        double prixUnitaire = (visite.getCircuit() != null && visite.getCircuit().getPrix() != null
                && visite.getCircuit().getPrix() > 0)
                ? visite.getCircuit().getPrix()
                : TARIF_VISITE_STANDARD;

        Reservation reservation = new Reservation();
        reservation.setNomReservant(touriste.getNom());
        reservation.setEmailReservant(touriste.getEmail());
        reservation.setTelephone(touriste.getTelephone() != null && !touriste.getTelephone().isBlank() ? touriste.getTelephone() : "+221 78 456 78 90");
        reservation.setNombrePlaces(1);
        reservation.setModePaiement("Wave");

        model.addAttribute("visite", visite);
        model.addAttribute("reservation", reservation);
        model.addAttribute("prixUnitaire", prixUnitaire);
        return "reservations/form";
    }

    @PostMapping("/reserver/{visiteId}")
    @Transactional
    public String reserver(@PathVariable Long visiteId,
                           @RequestParam String nomReservant,
                           @RequestParam String emailReservant,
                           @RequestParam String telephone,
                           @RequestParam(defaultValue = "1") int nombrePlaces,
                           @RequestParam(defaultValue = "Wave") String modePaiement,
                           @RequestParam(required = false) String commentaire,
                           @AuthenticationPrincipal CustomUserDetails principal,
                           RedirectAttributes redirectAttributes) {

        Visite visite = visiteRepository.findById(visiteId)
                .orElseThrow(() -> new IllegalArgumentException("Visite introuvable"));

        if (nombrePlaces < 1) {
            redirectAttributes.addFlashAttribute("messageErreur", "Le nombre de places doit être au moins 1.");
            return "redirect:/reservations/nouvelle/" + visiteId;
        }

        if (visite.isComplete() || nombrePlaces > visite.getPlacesDisponibles()) {
            redirectAttributes.addFlashAttribute("messagePatience",
                    "Places insuffisantes : il reste uniquement " + Math.max(0, visite.getPlacesDisponibles()) + " place(s) disponible(s).");
            return "redirect:/visites";
        }

        User touriste = userRepository.findById(principal.getUser().getId()).orElseThrow();
        double prixUnitaire = (visite.getCircuit() != null && visite.getCircuit().getPrix() != null
                && visite.getCircuit().getPrix() > 0)
                ? visite.getCircuit().getPrix()
                : TARIF_VISITE_STANDARD;

        Reservation reservation = new Reservation();
        reservation.setTouriste(touriste);
        reservation.setVisite(visite);
        reservation.setStatut(StatutReservation.CONFIRME);
        reservation.setNomReservant(nomReservant != null && !nomReservant.isBlank() ? nomReservant.trim() : touriste.getNom());
        reservation.setEmailReservant(emailReservant != null && !emailReservant.isBlank() ? emailReservant.trim() : touriste.getEmail());
        reservation.setTelephone(telephone != null && !telephone.isBlank() ? telephone.trim() : (touriste.getTelephone() != null ? touriste.getTelephone() : "+221 78 456 78 90"));
        reservation.setNombrePlaces(nombrePlaces);
        reservation.setModePaiement(modePaiement);
        reservation.setCommentaire(commentaire != null && !commentaire.isBlank() ? commentaire.trim() : null);
        reservation.setMontant(prixUnitaire * nombrePlaces);
        Reservation saved = reservationRepository.save(reservation);

        visite.setPlacesReservees(visite.getPlacesReservees() + nombrePlaces);
        visiteRepository.save(visite);

        microserviceGatewayService.synchroniserReservation(saved, visite, touriste);

        redirectAttributes.addFlashAttribute("messageSucces",
                "Réservation confirmée pour " + nombrePlaces + " place(s) au nom de " + saved.getNomReservant() + " !");
        return "redirect:/reservations";
    }

    @PostMapping("/annuler/{id}")
    @Transactional
    public String annuler(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        int placesALiberer = reservation.getNombrePlaces() != null && reservation.getNombrePlaces() > 0
                ? reservation.getNombrePlaces() : 1;

        Visite visite = reservation.getVisite();
        if (visite != null) {
            if (reservation.getStatut() == StatutReservation.CONFIRME) {
                visite.setPlacesReservees(Math.max(0, visite.getPlacesReservees() - placesALiberer));
            }
            visite.getReservations().removeIf(r -> id.equals(r.getId()));
            visiteRepository.save(visite);
        }

        reservationRepository.delete(reservation);
        microserviceGatewayService.supprimerReservation(id);

        redirectAttributes.addFlashAttribute("messageSucces",
                "Réservation annulée et supprimée de votre liste (" + placesALiberer + " place(s) libérée(s)).");
        return "redirect:/reservations";
    }
}

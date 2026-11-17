package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Avis;
import com.ziguinchor.patrimoine.model.SiteTouristique;
import com.ziguinchor.patrimoine.service.AvisService;
import com.ziguinchor.patrimoine.service.CategorieService;
import com.ziguinchor.patrimoine.service.MicroserviceGatewayService;
import com.ziguinchor.patrimoine.service.SiteTouristiqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sites")
public class SiteTouristiqueController {

    private final SiteTouristiqueService siteTouristiqueService;
    private final CategorieService categorieService;
    private final AvisService avisService;
    private final MicroserviceGatewayService microserviceGatewayService;

    @Autowired
    public SiteTouristiqueController(SiteTouristiqueService siteTouristiqueService,
                                      CategorieService categorieService,
                                      AvisService avisService,
                                      MicroserviceGatewayService microserviceGatewayService) {
        this.siteTouristiqueService = siteTouristiqueService;
        this.categorieService = categorieService;
        this.avisService = avisService;
        this.microserviceGatewayService = microserviceGatewayService;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) Long categorie,
                        @RequestParam(required = false) String recherche,
                        @RequestParam(required = false) Double lat,
                        @RequestParam(required = false) Double lon,
                        @RequestParam(defaultValue = "30") Double rayon,
                        Model model) {
        microserviceGatewayService.importerPatrimoinesDepuisMicroservice();
        if (lat != null && lon != null) {
            model.addAttribute("sites", siteTouristiqueService.rechercherParProximite(lat, lon, rayon));
            model.addAttribute("modeProximite", true);
        } else if (recherche != null && !recherche.isBlank()) {
            model.addAttribute("sites", siteTouristiqueService.rechercherParNom(recherche));
        } else if (categorie != null) {
            model.addAttribute("sites", siteTouristiqueService.listerParCategorie(categorie));
        } else {
            model.addAttribute("sites", siteTouristiqueService.listerTous());
        }
        model.addAttribute("categories", categorieService.listerToutes());
        model.addAttribute("categorieSelectionnee", categorie);
        model.addAttribute("recherche", recherche);
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        model.addAttribute("rayon", rayon);
        return "sites/liste";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        SiteTouristique site = siteTouristiqueService.trouverParId(id);
        model.addAttribute("site", site);
        model.addAttribute("nouvelAvis", new Avis());
        return "sites/detail";
    }

    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("site", new SiteTouristique());
        model.addAttribute("categories", categorieService.listerToutes());
        return "sites/form";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        model.addAttribute("site", siteTouristiqueService.trouverParId(id));
        model.addAttribute("categories", categorieService.listerToutes());
        return "sites/form";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("site") SiteTouristique site,
                               BindingResult resultat,
                               Model model) {
        if (resultat.hasErrors()) {
            model.addAttribute("categories", categorieService.listerToutes());
            return "sites/form";
        }
        SiteTouristique saved = siteTouristiqueService.enregistrer(site);
        microserviceGatewayService.synchroniserPatrimoine(saved);
        return "redirect:/sites";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        siteTouristiqueService.supprimer(id);
        microserviceGatewayService.supprimerPatrimoine(id);
        return "redirect:/sites";
    }

    @PostMapping("/{id}/horaires")
    public String modifierHoraires(@PathVariable Long id, @RequestParam String horairesOuverture) {
        SiteTouristique site = siteTouristiqueService.trouverParId(id);
        site.setHorairesOuverture(horairesOuverture);
        SiteTouristique saved = siteTouristiqueService.enregistrer(site);
        microserviceGatewayService.synchroniserPatrimoine(saved);
        return "redirect:/sites/" + id + "?horaires=ok";
    }

    @PostMapping("/{id}/avis")
    public String ajouterAvis(@PathVariable Long id,
                              @ModelAttribute("nouvelAvis") Avis avis,
                              @RequestParam(required = false) String redirectTo) {
        SiteTouristique site = siteTouristiqueService.trouverParId(id);
        avis.setSiteTouristique(site);
        Avis saved = avisService.enregistrer(avis);
        microserviceGatewayService.synchroniserAvis(id, saved);
        if ("reservations".equals(redirectTo)) {
            return "redirect:/reservations?avis=ok";
        }
        return "redirect:/sites/" + id;
    }
}

package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.service.MicroserviceGatewayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/microservices")
public class MicroserviceStatusController {

    private final MicroserviceGatewayService microserviceGatewayService;

    public MicroserviceStatusController(MicroserviceGatewayService microserviceGatewayService) {
        this.microserviceGatewayService = microserviceGatewayService;
    }

    @GetMapping
    public String afficherStatut(Model model) {
        List<MicroserviceGatewayService.MicroserviceStatut> statuts =
                microserviceGatewayService.verifierEtatMicroservices();
        long nbEnLigne = statuts.stream().filter(MicroserviceGatewayService.MicroserviceStatut::isEnLigne).count();
        model.addAttribute("statuts", statuts);
        model.addAttribute("nbEnLigne", nbEnLigne);
        model.addAttribute("nbTotal", statuts.size());
        return "microservices/statut";
    }
}

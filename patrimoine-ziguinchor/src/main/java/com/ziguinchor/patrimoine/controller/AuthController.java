package com.ziguinchor.patrimoine.controller;

import com.ziguinchor.patrimoine.model.Role;
import com.ziguinchor.patrimoine.model.StatutValidation;
import com.ziguinchor.patrimoine.model.User;
import com.ziguinchor.patrimoine.repository.UserRepository;
import com.ziguinchor.patrimoine.service.MicroserviceGatewayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MicroserviceGatewayService microserviceGatewayService;

    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          MicroserviceGatewayService microserviceGatewayService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.microserviceGatewayService = microserviceGatewayService;
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/register")
    public String formulaireInscription(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String inscrire(@Valid @ModelAttribute("user") User user,
                            BindingResult resultat,
                            @RequestParam("roleChoisi") String roleChoisi,
                            Model model) {
        if (resultat.hasErrors()) return "auth/register";

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("erreurEmail", "Cet email est déjà utilisé.");
            return "auth/register";
        }

        String motDePasseBrut = user.getMotDePasse();
        user.setMotDePasse(passwordEncoder.encode(motDePasseBrut));
        Role role = Role.valueOf(roleChoisi);
        user.setRole(role);
        if (role == Role.GUIDE) {
            user.setStatutValidation(StatutValidation.EN_ATTENTE);
            user.setActif(false);
        } else {
            user.setStatutValidation(StatutValidation.VALIDE);
            user.setActif(true);
        }
        User saved = userRepository.save(user);
        microserviceGatewayService.synchroniserUtilisateur(saved, motDePasseBrut);

        if (role == Role.GUIDE) {
            return "redirect:/login?inscriptionGuide=attente";
        }
        return "redirect:/login?inscription=succes";
    }
}

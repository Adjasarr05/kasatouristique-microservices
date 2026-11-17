package com.uasz.ms_utilisateur.config;

import com.uasz.ms_utilisateur.entities.Role;
import com.uasz.ms_utilisateur.entities.Utilisateur;
import com.uasz.ms_utilisateur.repositories.UtilisateurRepository;
import com.uasz.ms_utilisateur.services.UtilisateurService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurService utilisateurService;

    public DonneesInitiales(UtilisateurRepository utilisateurRepository,
                            UtilisateurService utilisateurService) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurService = utilisateurService;
    }

    @Override
    public void run(String... args) {
        creerCompteSiAbsent("conservateur@ziguinchor.sn", "Conservateur Test", Role.CONSERVATEUR, "+221 77 100 00 01");
        creerCompteSiAbsent("operateur@ziguinchor.sn", "Opérateur Touristique Test", Role.OPERATEUR_TOURISTIQUE, "+221 77 100 00 02");
        creerCompteSiAbsent("admincommunal@ziguinchor.sn", "Administrateur Communal Test", Role.ADMINISTRATEUR_COMMUNAL, "+221 77 100 00 03");
        creerCompteSiAbsent("superadmin@ziguinchor.sn", "Super Admin Test", Role.SUPER_ADMIN, "+221 77 100 00 04");
        creerCompteSiAbsent("visiteur@ziguinchor.sn", "Visiteur Test", Role.VISITEUR, "+221 77 100 00 05");
    }

    private void creerCompteSiAbsent(String email, String nom, Role role, String telephone) {
        if (!utilisateurRepository.existsByEmail(email)) {
            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setEmail(email);
            u.setMotDePasse("admin123");
            u.setRole(role);
            u.setTelephone(telephone);
            u.setActif(true);
            utilisateurService.saveUtilisateur(u);
        }
    }
}

package com.uasz.ms_utilisateur.services;

import com.uasz.ms_utilisateur.entities.Role;
import com.uasz.ms_utilisateur.entities.Utilisateur;
import com.uasz.ms_utilisateur.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UtilisateurService implements IUtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public String hacherMotDePasse(String brut) {
        if (brut == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(brut.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithme SHA-256 indisponible", e);
        }
    }

    @Override
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getRole() == null) {
            utilisateur.setRole(Role.VISITEUR);
        }
        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(hacherMotDePasse(utilisateur.getMotDePasse()));
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur inscrire(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }
        utilisateur.setActif(true);
        return saveUtilisateur(utilisateur);
    }

    @Override
    public Map<String, Object> connecter(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Identifiants incorrects."));
        if (!utilisateur.isActif()) {
            throw new IllegalStateException("Ce compte est désactivé.");
        }
        String hash = hacherMotDePasse(motDePasse);
        if (utilisateur.getMotDePasse() == null
                || (!utilisateur.getMotDePasse().equals(hash) && !utilisateur.getMotDePasse().equals(motDePasse))) {
            throw new IllegalArgumentException("Identifiants incorrects.");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("authentifie", true);
        result.put("utilisateur", utilisateur);
        return result;
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public List<Utilisateur> getByRole(Role role) {
        return utilisateurRepository.findByRole(role);
    }

    @Override
    public Optional<Utilisateur> getByIdUtilisateur(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> getByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public Utilisateur updateUtilisateur(Long id, Utilisateur newUtilisateur) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setNom(newUtilisateur.getNom());
            if (newUtilisateur.getEmail() != null) {
                u.setEmail(newUtilisateur.getEmail());
            }
            if (newUtilisateur.getTelephone() != null) {
                u.setTelephone(newUtilisateur.getTelephone());
            }
            if (newUtilisateur.getRole() != null) {
                u.setRole(newUtilisateur.getRole());
            }
            if (newUtilisateur.getMotDePasse() != null && !newUtilisateur.getMotDePasse().isBlank()) {
                u.setMotDePasse(hacherMotDePasse(newUtilisateur.getMotDePasse()));
            }
            u.setActif(newUtilisateur.isActif());
            return utilisateurRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }

    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}

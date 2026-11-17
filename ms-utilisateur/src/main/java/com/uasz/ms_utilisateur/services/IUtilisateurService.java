package com.uasz.ms_utilisateur.services;

import com.uasz.ms_utilisateur.entities.Role;
import com.uasz.ms_utilisateur.entities.Utilisateur;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUtilisateurService {
    Utilisateur saveUtilisateur(Utilisateur utilisateur);

    Utilisateur inscrire(Utilisateur utilisateur);

    Map<String, Object> connecter(String email, String motDePasse);

    List<Utilisateur> getAllUtilisateurs();

    List<Utilisateur> getByRole(Role role);

    Optional<Utilisateur> getByIdUtilisateur(Long id);

    Optional<Utilisateur> getByEmail(String email);

    Utilisateur updateUtilisateur(Long id, Utilisateur newUtilisateur);

    void deleteUtilisateur(Long id);
}

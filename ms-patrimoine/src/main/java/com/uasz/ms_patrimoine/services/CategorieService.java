package com.uasz.ms_patrimoine.services;

import com.uasz.ms_patrimoine.entities.Categorie;
import com.uasz.ms_patrimoine.repositories.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Optional<Categorie> getByIdCategorie(Long id) {
        return categorieRepository.findById(id);
    }

    public Categorie updateCategorie(Long id, Categorie updated) {
        return categorieRepository.findById(id).map(cat -> {
            cat.setNom(updated.getNom());
            cat.setDescription(updated.getDescription());
            return categorieRepository.save(cat);
        }).orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + id));
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }
}

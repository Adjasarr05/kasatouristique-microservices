package com.uasz.ms_patrimoine.services;

import com.uasz.ms_patrimoine.entities.Avis;
import com.uasz.ms_patrimoine.entities.Categorie;
import com.uasz.ms_patrimoine.entities.Patrimoine;
import com.uasz.ms_patrimoine.repositories.AvisRepository;
import com.uasz.ms_patrimoine.repositories.CategorieRepository;
import com.uasz.ms_patrimoine.repositories.PatrimoineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatrimoineService implements IPatrimoineService {

    private final PatrimoineRepository patrimoineRepository;
    private final CategorieRepository categorieRepository;
    private final AvisRepository avisRepository;

    @Autowired
    public PatrimoineService(PatrimoineRepository patrimoineRepository,
                             CategorieRepository categorieRepository,
                             AvisRepository avisRepository) {
        this.patrimoineRepository = patrimoineRepository;
        this.categorieRepository = categorieRepository;
        this.avisRepository = avisRepository;
    }

    @Override
    public Patrimoine savePatrimoine(Patrimoine patrimoine) {
        if (patrimoine.getCategorie() != null && patrimoine.getCategorie().getId() != null) {
            Categorie cat = categorieRepository.findById(patrimoine.getCategorie().getId())
                    .orElse(null);
            patrimoine.setCategorie(cat);
        }
        return patrimoineRepository.save(patrimoine);
    }

    @Override
    public List<Patrimoine> getAllPatrimoines() {
        return patrimoineRepository.findAll();
    }

    @Override
    public Optional<Patrimoine> getByIdPatrimoine(Long id) {
        return patrimoineRepository.findById(id);
    }

    @Override
    public Patrimoine updatePatrimoine(Long id, Patrimoine newPatrimoine) {
        return patrimoineRepository.findById(id).map(patrimoine -> {
            patrimoine.setAdressePatrimoine(newPatrimoine.getAdressePatrimoine());
            patrimoine.setCodePatrimoine(newPatrimoine.getCodePatrimoine());
            patrimoine.setCapacitePatrimoine(newPatrimoine.getCapacitePatrimoine());
            patrimoine.setDescriptionPatrimoine(newPatrimoine.getDescriptionPatrimoine());
            patrimoine.setHistoriquePatrimoine(newPatrimoine.getHistoriquePatrimoine());
            patrimoine.setNomPatrimoine(newPatrimoine.getNomPatrimoine());
            if (newPatrimoine.getQuartier() != null) {
                patrimoine.setQuartier(newPatrimoine.getQuartier());
            }
            if (newPatrimoine.getHorairesOuverture() != null) {
                patrimoine.setHorairesOuverture(newPatrimoine.getHorairesOuverture());
            }
            if (newPatrimoine.getImageUrl() != null) {
                patrimoine.setImageUrl(newPatrimoine.getImageUrl());
            }
            if (newPatrimoine.getLatitude() != null) {
                patrimoine.setLatitude(newPatrimoine.getLatitude());
            }
            if (newPatrimoine.getLongitude() != null) {
                patrimoine.setLongitude(newPatrimoine.getLongitude());
            }
            if (newPatrimoine.getCategorie() != null && newPatrimoine.getCategorie().getId() != null) {
                Categorie cat = categorieRepository.findById(newPatrimoine.getCategorie().getId()).orElse(null);
                patrimoine.setCategorie(cat);
            }
            return patrimoineRepository.save(patrimoine);
        }).orElseThrow(() -> new RuntimeException("Patrimoine non trouvé avec l'id : " + id));
    }

    @Override
    public void deletePatrimoine(Long id) {
        patrimoineRepository.deleteById(id);
    }

    @Override
    public List<Patrimoine> rechercherParNom(String motCle) {
        return patrimoineRepository.findByNomContainingIgnoreCase(motCle);
    }

    @Override
    public List<Patrimoine> getByCategorie(Long categorieId) {
        return patrimoineRepository.findByCategorieId(categorieId);
    }

    @Override
    public Avis ajouterAvis(Long patrimoineId, Avis avis) {
        Patrimoine patrimoine = patrimoineRepository.findById(patrimoineId)
                .orElseThrow(() -> new RuntimeException("Patrimoine non trouvé avec l'id : " + patrimoineId));
        avis.setPatrimoine(patrimoine);
        return avisRepository.save(avis);
    }
}

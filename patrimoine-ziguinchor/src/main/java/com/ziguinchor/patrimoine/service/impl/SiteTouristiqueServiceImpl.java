package com.ziguinchor.patrimoine.service.impl;

import com.ziguinchor.patrimoine.model.SiteTouristique;
import com.ziguinchor.patrimoine.repository.SiteTouristiqueRepository;
import com.ziguinchor.patrimoine.service.SiteTouristiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiteTouristiqueServiceImpl implements SiteTouristiqueService {

    private final SiteTouristiqueRepository siteTouristiqueRepository;

    @Autowired
    public SiteTouristiqueServiceImpl(SiteTouristiqueRepository siteTouristiqueRepository) {
        this.siteTouristiqueRepository = siteTouristiqueRepository;
    }

    @Override
    public List<SiteTouristique> listerTous() { return siteTouristiqueRepository.findAll(); }

    @Override
    public SiteTouristique trouverParId(Long id) {
        return siteTouristiqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Site touristique introuvable avec l'id : " + id));
    }

    @Override
    public SiteTouristique enregistrer(SiteTouristique site) { return siteTouristiqueRepository.save(site); }

    @Override
    public void supprimer(Long id) { siteTouristiqueRepository.deleteById(id); }

    @Override
    public List<SiteTouristique> rechercherParNom(String motCle) { return siteTouristiqueRepository.findByNomContainingIgnoreCase(motCle); }

    @Override
    public List<SiteTouristique> listerParCategorie(Long categorieId) { return siteTouristiqueRepository.findByCategorieId(categorieId); }

    @Override
    public List<SiteTouristique> rechercherParProximite(double latitude, double longitude, double rayonKm) {
        return siteTouristiqueRepository.findAll().stream()
                .filter(s -> s.getLatitude() != null && s.getLongitude() != null)
                .peek(s -> s.setDistanceKm(calculerDistanceKm(latitude, longitude, s.getLatitude(), s.getLongitude())))
                .filter(s -> s.getDistanceKm() <= rayonKm)
                .sorted(Comparator.comparingDouble(SiteTouristique::getDistanceKm))
                .collect(Collectors.toList());
    }

    private double calculerDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((R * c) * 100.0) / 100.0;
    }
}

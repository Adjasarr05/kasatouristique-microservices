package com.uasz.ms_patrimoine.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patrimoine")
public class Patrimoine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String code;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String historique;

    private String adresse;
    private String quartier;
    private int capacite;
    private String horairesOuverture;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private LocalDate dateAjout;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToMany(mappedBy = "patrimoine", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Avis> avis = new ArrayList<>();

    public Patrimoine() {
    }

    public Patrimoine(Long id, String nom, String code, String description, String historique,
                      String adresse, String quartier, int capacite, String horairesOuverture,
                      String imageUrl, Double latitude, Double longitude, LocalDate dateAjout,
                      Categorie categorie, List<Avis> avis) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.description = description;
        this.historique = historique;
        this.adresse = adresse;
        this.quartier = quartier;
        this.capacite = capacite;
        this.horairesOuverture = horairesOuverture;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateAjout = dateAjout;
        this.categorie = categorie;
        this.avis = avis != null ? avis : new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        if (dateAjout == null) {
            dateAjout = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHistorique() {
        return historique;
    }

    public void setHistorique(String historique) {
        this.historique = historique;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getHorairesOuverture() {
        return horairesOuverture;
    }

    public void setHorairesOuverture(String horairesOuverture) {
        this.horairesOuverture = horairesOuverture;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Avis> getAvis() {
        return avis;
    }

    public void setAvis(List<Avis> avis) {
        this.avis = avis;
    }

    // Délégués de compatibilité JSON stricte avec les attributs du TP1 (Diapo 24 & 34)
    public String getNomPatrimoine() {
        return nom;
    }

    public void setNomPatrimoine(String nomPatrimoine) {
        this.nom = nomPatrimoine;
    }

    public String getCodePatrimoine() {
        return code;
    }

    public void setCodePatrimoine(String codePatrimoine) {
        this.code = codePatrimoine;
    }

    public String getDescriptionPatrimoine() {
        return description;
    }

    public void setDescriptionPatrimoine(String descriptionPatrimoine) {
        this.description = descriptionPatrimoine;
    }

    public String getHistoriquePatrimoine() {
        return historique;
    }

    public void setHistoriquePatrimoine(String historiquePatrimoine) {
        this.historique = historiquePatrimoine;
    }

    public String getAdressePatrimoine() {
        return adresse;
    }

    public void setAdressePatrimoine(String adressePatrimoine) {
        this.adresse = adressePatrimoine;
    }

    public int getCapacitePatrimoine() {
        return capacite;
    }

    public void setCapacitePatrimoine(int capacitePatrimoine) {
        this.capacite = capacitePatrimoine;
    }

    public double getNoteMoyenne() {
        if (avis == null || avis.isEmpty()) {
            return 0;
        }
        return avis.stream().mapToInt(Avis::getNote).average().orElse(0);
    }
}

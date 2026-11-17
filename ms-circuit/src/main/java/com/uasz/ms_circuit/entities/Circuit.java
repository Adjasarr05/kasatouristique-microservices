package com.uasz.ms_circuit.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "circuit")
public class Circuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(length = 1000)
    private String description;

    private int dureeJours = 1;

    private String dureeEstimee;

    private Double distanceKm;

    private String difficulte;

    private Double prix = 0.0;

    @Enumerated(EnumType.STRING)
    private StatutCircuit statut = StatutCircuit.BROUILLON;

    private int capaciteMax = 20;

    private int placesReservees = 0;

    private Long operateurId;

    private String operateurNom;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "circuit_patrimoines", joinColumns = @JoinColumn(name = "circuit_id"))
    @OrderColumn(name = "position")
    @Column(name = "patrimoine_id")
    private List<Long> patrimoineIds = new ArrayList<>();

    @OneToMany(mappedBy = "circuit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("ordre ASC")
    private List<EtapeCircuit> etapes = new ArrayList<>();

    public Circuit() {
    }

    public Circuit(Long id, String nom, String description, int dureeJours, String dureeEstimee,
                   Double distanceKm, String difficulte, Double prix, StatutCircuit statut,
                   int capaciteMax, int placesReservees, Long operateurId, String operateurNom,
                   List<Long> patrimoineIds, List<EtapeCircuit> etapes) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dureeJours = dureeJours;
        this.dureeEstimee = dureeEstimee;
        this.distanceKm = distanceKm;
        this.difficulte = difficulte;
        this.prix = prix;
        this.statut = statut != null ? statut : StatutCircuit.BROUILLON;
        this.capaciteMax = capaciteMax;
        this.placesReservees = placesReservees;
        this.operateurId = operateurId;
        this.operateurNom = operateurNom;
        this.patrimoineIds = patrimoineIds != null ? patrimoineIds : new ArrayList<>();
        this.etapes = etapes != null ? etapes : new ArrayList<>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(int dureeJours) {
        this.dureeJours = dureeJours;
    }

    public String getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(String dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public StatutCircuit getStatut() {
        return statut;
    }

    public void setStatut(StatutCircuit statut) {
        this.statut = statut;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public int getPlacesReservees() {
        return placesReservees;
    }

    public void setPlacesReservees(int placesReservees) {
        this.placesReservees = placesReservees;
    }

    public Long getOperateurId() {
        return operateurId;
    }

    public void setOperateurId(Long operateurId) {
        this.operateurId = operateurId;
    }

    public String getOperateurNom() {
        return operateurNom;
    }

    public void setOperateurNom(String operateurNom) {
        this.operateurNom = operateurNom;
    }

    public List<Long> getPatrimoineIds() {
        return patrimoineIds;
    }

    public void setPatrimoineIds(List<Long> patrimoineIds) {
        this.patrimoineIds = patrimoineIds;
    }

    public List<EtapeCircuit> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<EtapeCircuit> etapes) {
        this.etapes = etapes;
        if (this.etapes != null) {
            for (EtapeCircuit etape : this.etapes) {
                etape.setCircuit(this);
            }
        }
    }

    public int getPlacesDisponibles() {
        return Math.max(0, capaciteMax - placesReservees);
    }

    public boolean isDisponible() {
        return statut == StatutCircuit.PUBLIE && getPlacesDisponibles() > 0;
    }
}

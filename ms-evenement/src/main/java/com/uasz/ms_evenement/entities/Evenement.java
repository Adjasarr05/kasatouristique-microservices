package com.uasz.ms_evenement.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evenement")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String typeEvenement = "VISITE_GUIDEE";

    @Column(length = 1500)
    private String description;

    private String lieuPrecis;

    private Long patrimoineId;

    private String patrimoineNom;

    private Long circuitId;

    private String circuitNom;

    private String guide;

    private LocalDateTime dateHeureDebut;

    private int dureeMinutes = 60;

    private int capaciteMax = 15;

    private int placesReservees = 0;

    private Double prix = 0.0;

    @Enumerated(EnumType.STRING)
    private StatutEvenement statut = StatutEvenement.PLANIFIEE;

    public Evenement() {
    }

    public Evenement(Long id, String titre, String typeEvenement, String description, String lieuPrecis,
                     Long patrimoineId, String patrimoineNom, Long circuitId, String circuitNom,
                     String guide, LocalDateTime dateHeureDebut, int dureeMinutes, int capaciteMax,
                     int placesReservees, Double prix, StatutEvenement statut) {
        this.id = id;
        this.titre = titre;
        this.typeEvenement = typeEvenement;
        this.description = description;
        this.lieuPrecis = lieuPrecis;
        this.patrimoineId = patrimoineId;
        this.patrimoineNom = patrimoineNom;
        this.circuitId = circuitId;
        this.circuitNom = circuitNom;
        this.guide = guide;
        this.dateHeureDebut = dateHeureDebut;
        this.dureeMinutes = dureeMinutes;
        this.capaciteMax = capaciteMax;
        this.placesReservees = placesReservees;
        this.prix = prix;
        this.statut = statut != null ? statut : StatutEvenement.PLANIFIEE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieuPrecis() {
        return lieuPrecis;
    }

    public void setLieuPrecis(String lieuPrecis) {
        this.lieuPrecis = lieuPrecis;
    }

    public Long getPatrimoineId() {
        return patrimoineId;
    }

    public void setPatrimoineId(Long patrimoineId) {
        this.patrimoineId = patrimoineId;
    }

    public String getPatrimoineNom() {
        return patrimoineNom;
    }

    public void setPatrimoineNom(String patrimoineNom) {
        this.patrimoineNom = patrimoineNom;
    }

    public Long getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(Long circuitId) {
        this.circuitId = circuitId;
    }

    public String getCircuitNom() {
        return circuitNom;
    }

    public void setCircuitNom(String circuitNom) {
        this.circuitNom = circuitNom;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(int dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public int getPlacesMax() {
        return capaciteMax;
    }

    public void setPlacesMax(int placesMax) {
        this.capaciteMax = placesMax;
    }

    public int getPlacesReservees() {
        return placesReservees;
    }

    public void setPlacesReservees(int placesReservees) {
        this.placesReservees = placesReservees;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public StatutEvenement getStatut() {
        return statut;
    }

    public void setStatut(StatutEvenement statut) {
        this.statut = statut;
    }

    public int getPlacesDisponibles() {
        return Math.max(0, capaciteMax - placesReservees);
    }

    public boolean isComplete() {
        return statut != StatutEvenement.PLANIFIEE || getPlacesDisponibles() <= 0;
    }
}

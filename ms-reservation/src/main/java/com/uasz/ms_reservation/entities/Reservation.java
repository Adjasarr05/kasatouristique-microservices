package com.uasz.ms_reservation.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId;

    private String utilisateurNom;

    private String utilisateurEmail;

    private String typeReservation = "EVENEMENT";

    private Long evenementId;

    private Long circuitId;

    private String nomCible;

    private LocalDateTime dateHeureCible;

    private int nombrePlaces = 1;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut = StatutReservation.CONFIRME;

    @Column(unique = true)
    private String qrCodeHash;

    private Double montant = 0.0;

    private LocalDateTime dateReservation;

    public Reservation() {
    }

    public Reservation(Long id, Long utilisateurId, String utilisateurNom, String utilisateurEmail,
                       String typeReservation, Long evenementId, Long circuitId, String nomCible,
                       LocalDateTime dateHeureCible, int nombrePlaces, StatutReservation statut,
                       String qrCodeHash, Double montant, LocalDateTime dateReservation) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.utilisateurNom = utilisateurNom;
        this.utilisateurEmail = utilisateurEmail;
        this.typeReservation = typeReservation;
        this.evenementId = evenementId;
        this.circuitId = circuitId;
        this.nomCible = nomCible;
        this.dateHeureCible = dateHeureCible;
        this.nombrePlaces = nombrePlaces;
        this.statut = statut != null ? statut : StatutReservation.CONFIRME;
        this.qrCodeHash = qrCodeHash;
        this.montant = montant;
        this.dateReservation = dateReservation;
    }

    @PrePersist
    public void prePersist() {
        if (dateReservation == null) {
            dateReservation = LocalDateTime.now();
        }
        if (qrCodeHash == null || qrCodeHash.isBlank()) {
            qrCodeHash = UUID.randomUUID().toString();
        }
        if (statut == null) {
            statut = StatutReservation.CONFIRME;
        }
        if (nombrePlaces <= 0) {
            nombrePlaces = 1;
        }
        if (montant == null) {
            montant = 0.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

    public String getUtilisateurEmail() {
        return utilisateurEmail;
    }

    public void setUtilisateurEmail(String utilisateurEmail) {
        this.utilisateurEmail = utilisateurEmail;
    }

    public String getTypeReservation() {
        return typeReservation;
    }

    public void setTypeReservation(String typeReservation) {
        this.typeReservation = typeReservation;
    }

    public Long getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Long evenementId) {
        this.evenementId = evenementId;
    }

    public Long getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(Long circuitId) {
        this.circuitId = circuitId;
    }

    public String getNomCible() {
        return nomCible;
    }

    public void setNomCible(String nomCible) {
        this.nomCible = nomCible;
    }

    public LocalDateTime getDateHeureCible() {
        return dateHeureCible;
    }

    public void setDateHeureCible(LocalDateTime dateHeureCible) {
        this.dateHeureCible = dateHeureCible;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public String getQrCodeHash() {
        return qrCodeHash;
    }

    public void setQrCodeHash(String qrCodeHash) {
        this.qrCodeHash = qrCodeHash;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }
}

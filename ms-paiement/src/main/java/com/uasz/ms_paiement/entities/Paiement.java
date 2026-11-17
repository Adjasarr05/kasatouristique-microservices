package com.uasz.ms_paiement.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "paiement")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String referenceTransaction;

    private Long reservationId;

    private Long utilisateurId;

    private Double montant;

    private String modePaiement = "MOBILE_MONEY";

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut = StatutPaiement.VALIDE;

    private LocalDateTime datePaiement;

    public Paiement() {
    }

    public Paiement(Long id, String referenceTransaction, Long reservationId, Long utilisateurId,
                    Double montant, String modePaiement, StatutPaiement statut, LocalDateTime datePaiement) {
        this.id = id;
        this.referenceTransaction = referenceTransaction;
        this.reservationId = reservationId;
        this.utilisateurId = utilisateurId;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.statut = statut != null ? statut : StatutPaiement.VALIDE;
        this.datePaiement = datePaiement;
    }

    @PrePersist
    public void prePersist() {
        if (referenceTransaction == null || referenceTransaction.isBlank()) {
            referenceTransaction = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        if (datePaiement == null) {
            datePaiement = LocalDateTime.now();
        }
        if (modePaiement == null || modePaiement.isBlank()) {
            modePaiement = "MOBILE_MONEY";
        }
        if (statut == null) {
            statut = StatutPaiement.VALIDE;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceTransaction() {
        return referenceTransaction;
    }

    public void setReferenceTransaction(String referenceTransaction) {
        this.referenceTransaction = referenceTransaction;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }
}

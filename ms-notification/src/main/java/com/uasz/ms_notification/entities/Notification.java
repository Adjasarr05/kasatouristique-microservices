package com.uasz.ms_notification.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId;

    private String destinataire;

    private String canal = "EMAIL";

    private String typeNotification = "CONFIRMATION";

    private String sujet;

    @Column(length = 2000)
    private String message;

    private String statut = "ENVOYEE";

    private LocalDateTime dateEnvoi;

    public Notification() {
    }

    public Notification(Long id, Long utilisateurId, String destinataire, String canal,
                        String typeNotification, String sujet, String message,
                        String statut, LocalDateTime dateEnvoi) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.destinataire = destinataire;
        this.canal = canal;
        this.typeNotification = typeNotification;
        this.sujet = sujet;
        this.message = message;
        this.statut = statut;
        this.dateEnvoi = dateEnvoi;
    }

    @PrePersist
    public void prePersist() {
        if (dateEnvoi == null) {
            dateEnvoi = LocalDateTime.now();
        }
        if (canal == null || canal.isBlank()) {
            canal = "EMAIL";
        }
        if (typeNotification == null || typeNotification.isBlank()) {
            typeNotification = "INFO";
        }
        if (statut == null || statut.isBlank()) {
            statut = "ENVOYEE";
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

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}

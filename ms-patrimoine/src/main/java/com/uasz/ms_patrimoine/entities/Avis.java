package com.uasz.ms_patrimoine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "avis")
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'auteur est obligatoire")
    private String auteur;

    @NotBlank(message = "Le commentaire ne peut pas être vide")
    @Column(length = 1000)
    private String commentaire;

    @Min(1)
    @Max(5)
    private int note;

    private LocalDate dateAvis;

    @ManyToOne
    @JoinColumn(name = "patrimoine_id")
    @JsonIgnore
    private Patrimoine patrimoine;

    public Avis() {
    }

    public Avis(Long id, String auteur, String commentaire, int note, LocalDate dateAvis, Patrimoine patrimoine) {
        this.id = id;
        this.auteur = auteur;
        this.commentaire = commentaire;
        this.note = note;
        this.dateAvis = dateAvis;
        this.patrimoine = patrimoine;
    }

    @PrePersist
    public void prePersist() {
        if (dateAvis == null) {
            dateAvis = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public LocalDate getDateAvis() {
        return dateAvis;
    }

    public void setDateAvis(LocalDate dateAvis) {
        this.dateAvis = dateAvis;
    }

    public Patrimoine getPatrimoine() {
        return patrimoine;
    }

    public void setPatrimoine(Patrimoine patrimoine) {
        this.patrimoine = patrimoine;
    }
}

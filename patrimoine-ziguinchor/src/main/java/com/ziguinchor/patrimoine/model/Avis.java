package com.ziguinchor.patrimoine.model;

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

    @Min(1) @Max(5)
    private int note;

    private LocalDate dateAvis;

    @Column(length = 1000)
    private String reponseGuide;
    private String auteurReponse;
    private LocalDate dateReponse;

    private boolean signale = false;
    private String motifSignalement;
    private boolean masque = false;

    @ManyToOne
    @JoinColumn(name = "site_touristique_id")
    private SiteTouristique siteTouristique;

    public Avis() {}

    @PrePersist
    public void prePersist() {
        if (dateAvis == null) dateAvis = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }
    public LocalDate getDateAvis() { return dateAvis; }
    public void setDateAvis(LocalDate dateAvis) { this.dateAvis = dateAvis; }
    public String getReponseGuide() { return reponseGuide; }
    public void setReponseGuide(String reponseGuide) { this.reponseGuide = reponseGuide; }
    public String getAuteurReponse() { return auteurReponse; }
    public void setAuteurReponse(String auteurReponse) { this.auteurReponse = auteurReponse; }
    public LocalDate getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDate dateReponse) { this.dateReponse = dateReponse; }
    public boolean isSignale() { return signale; }
    public void setSignale(boolean signale) { this.signale = signale; }
    public String getMotifSignalement() { return motifSignalement; }
    public void setMotifSignalement(String motifSignalement) { this.motifSignalement = motifSignalement; }
    public boolean isMasque() { return masque; }
    public void setMasque(boolean masque) { this.masque = masque; }
    public SiteTouristique getSiteTouristique() { return siteTouristique; }
    public void setSiteTouristique(SiteTouristique siteTouristique) { this.siteTouristique = siteTouristique; }
}

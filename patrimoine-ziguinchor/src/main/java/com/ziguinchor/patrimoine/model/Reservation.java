package com.ziguinchor.patrimoine.model;

import jakarta.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "reservation")
public class Reservation {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "touriste_id")
    private User touriste;

    @ManyToOne
    @JoinColumn(name = "visite_id")
    private Visite visite;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut = StatutReservation.CONFIRME;

    @Column(unique = true)
    private String qrCodeHash;

    private Double montant = 0.0;

    private String nomReservant;
    private String emailReservant;
    private String telephone;
    private Integer nombrePlaces = 1;
    private String modePaiement = "Wave";

    @Column(length = 500)
    private String commentaire;

    private LocalDateTime dateReservation;

    public Reservation() {}

    @PrePersist
    public void prePersist() {
        if (dateReservation == null) dateReservation = LocalDateTime.now();
        if (qrCodeHash == null || qrCodeHash.isBlank()) qrCodeHash = genererCodeBillet();
        if (nombrePlaces == null || nombrePlaces < 1) nombrePlaces = 1;
    }

    private String genererCodeBillet() {
        StringBuilder sb = new StringBuilder("ZIG-2026-");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 4; i++) {
            sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        return sb.toString();
    }

    @Transient
    public String getContenuQrCode() {
        String siteNom = (visite != null && visite.getSite() != null && visite.getSite().getNom() != null)
                ? visite.getSite().getNom() : "Site touristique de Ziguinchor";
        String lieu = (visite != null && visite.getSite() != null && visite.getSite().getQuartier() != null)
                ? visite.getSite().getQuartier() : "Région de Ziguinchor";
        String dateVisite = (visite != null && visite.getDateHeureDebut() != null)
                ? visite.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"))
                : "À confirmer";
        String guide = (visite != null && visite.getGuide() != null && !visite.getGuide().isBlank())
                ? visite.getGuide() : "Guide local officiel";
        String circuitNom = (visite != null && visite.getCircuit() != null && visite.getCircuit().getNom() != null)
                ? visite.getCircuit().getNom() : "Visite guidée";
        String titulaire = (nomReservant != null && !nomReservant.isBlank())
                ? nomReservant : (touriste != null && touriste.getNom() != null ? touriste.getNom() : "Visiteur");
        String tel = (telephone != null && !telephone.isBlank()) ? " (" + telephone + ")" : "";
        int places = (nombrePlaces != null && nombrePlaces > 0) ? nombrePlaces : 1;
        String montantTxt = (montant != null && montant > 0) ? Math.round(montant) + " FCFA" : "Gratuit";
        String paiement = (modePaiement != null && !modePaiement.isBlank()) ? modePaiement : "Mobile Money";

        StringBuilder sb = new StringBuilder();
        sb.append("KASATOURISTIQUE - BILLET OFFICIEL\n");
        sb.append("Réf : ").append(qrCodeHash != null ? qrCodeHash : "N/A").append("\n");
        sb.append("Site réservé : ").append(siteNom).append("\n");
        sb.append("Lieu : ").append(lieu).append("\n");
        sb.append("Date visite : ").append(dateVisite).append("\n");
        sb.append("Guide : ").append(guide).append("\n");
        sb.append("Circuit : ").append(circuitNom).append("\n");
        sb.append("Réservé par : ").append(titulaire).append(tel).append("\n");
        sb.append("Places : ").append(places).append(" place(s)\n");
        sb.append("Montant : ").append(montantTxt).append(" (").append(paiement).append(")\n");
        sb.append("Statut : ").append(statut != null ? statut.name() : "CONFIRME");
        return sb.toString();
    }

    @Transient
    public String getUrlQrCode() {
        String encoded = URLEncoder.encode(getContenuQrCode(), StandardCharsets.UTF_8).replace("+", "%20");
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&margin=8&data=" + encoded;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getTouriste() { return touriste; }
    public void setTouriste(User touriste) { this.touriste = touriste; }
    public Visite getVisite() { return visite; }
    public void setVisite(Visite visite) { this.visite = visite; }
    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }
    public String getQrCodeHash() { return qrCodeHash; }
    public void setQrCodeHash(String qrCodeHash) { this.qrCodeHash = qrCodeHash; }
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public String getNomReservant() { return nomReservant; }
    public void setNomReservant(String nomReservant) { this.nomReservant = nomReservant; }
    public String getEmailReservant() { return emailReservant; }
    public void setEmailReservant(String emailReservant) { this.emailReservant = emailReservant; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Integer getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(Integer nombrePlaces) { this.nombrePlaces = nombrePlaces; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
}

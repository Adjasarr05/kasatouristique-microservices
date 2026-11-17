package com.uasz.ms_circuit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "etape_circuit")
public class EtapeCircuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ordre;

    private String nomEtape;

    @Column(length = 1000)
    private String description;

    private int dureeMinutes;

    private Long patrimoineId;

    @ManyToOne
    @JoinColumn(name = "circuit_id")
    @JsonIgnore
    private Circuit circuit;

    public EtapeCircuit() {
    }

    public EtapeCircuit(Long id, int ordre, String nomEtape, String description,
                        int dureeMinutes, Long patrimoineId, Circuit circuit) {
        this.id = id;
        this.ordre = ordre;
        this.nomEtape = nomEtape;
        this.description = description;
        this.dureeMinutes = dureeMinutes;
        this.patrimoineId = patrimoineId;
        this.circuit = circuit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public String getNomEtape() {
        return nomEtape;
    }

    public void setNomEtape(String nomEtape) {
        this.nomEtape = nomEtape;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(int dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Long getPatrimoineId() {
        return patrimoineId;
    }

    public void setPatrimoineId(Long patrimoineId) {
        this.patrimoineId = patrimoineId;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }
}

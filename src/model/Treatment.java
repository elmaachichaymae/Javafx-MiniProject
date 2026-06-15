package model;

import java.time.LocalDate;

public class Treatment {

    private int id;
    private String nom;
    private String type;
    private String posologie;
    private String effetsSecondaires;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean actif;
    private int dureeEstimee;        // en jours
    private int nombrePrisesJournalieres;

    // Constructeur vide
    public Treatment() {}

    // Constructeur complet
    public Treatment(int id, String nom, String type, String posologie,
                     String effetsSecondaires, LocalDate dateDebut,
                     LocalDate dateFin, boolean actif,
                     int dureeEstimee, int nombrePrisesJournalieres) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.posologie = posologie;
        this.effetsSecondaires = effetsSecondaires;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = actif;
        this.dureeEstimee = dureeEstimee;
        this.nombrePrisesJournalieres = nombrePrisesJournalieres;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }

    public String getEffetsSecondaires() { return effetsSecondaires; }
    public void setEffetsSecondaires(String effetsSecondaires) {
        this.effetsSecondaires = effetsSecondaires;
    }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public int getDureeEstimee() { return dureeEstimee; }
    public void setDureeEstimee(int dureeEstimee) { this.dureeEstimee = dureeEstimee; }

    public int getNombrePrisesJournalieres() { return nombrePrisesJournalieres; }
    public void setNombrePrisesJournalieres(int n) {
        this.nombrePrisesJournalieres = n;
    }

    @Override
    public String toString() {
        return "Treatment{id=" + id + ", nom='" + nom + "', type='" + type +
                "', actif=" + actif + ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin + "}";
    }
}
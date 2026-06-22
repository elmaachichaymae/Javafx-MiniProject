package model;

public class Patient {
    private int id;
    private String nom;
    private String dateNaissance;
    private String sexe;
    private String allergies; // Nouveau champ ajouté

    // Constructeur complet (utile pour les nouvelles insertions/modifications)
    public Patient(int id, String nom, String dateNaissance, String sexe, String allergies) {
        this.id = id;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.allergies = allergies;
    }

    // Constructeur simple (gardé pour la compatibilité avec DashboardController)
    public Patient(int id, String nom, String dateNaissance, String sexe) {
        this.id = id;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.allergies = "";
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
}
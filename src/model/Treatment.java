package model;

public class Treatment {
    private int id;
    private String medicament;
    private String type;
    private String posologie;
    private String dateFin;

    public Treatment(int id, String medicament, String type, String posologie, String dateFin) {
        this.id = id;
        this.medicament = medicament;
        this.type = type;
        this.posologie = posologie;
        this.dateFin = dateFin;
    }

    public int getId() { return id; }
    public String getMedicament() { return medicament; }
    public String getType() { return type; }
    public String getPosologie() { return posologie; }
    public String getDateFin() { return dateFin; }
}
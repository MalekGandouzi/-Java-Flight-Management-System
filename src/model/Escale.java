package model;

import java.time.LocalTime;

public class Escale {

    private int id;
    private int volId;                // ✅ lié à un vol
    private Aeroport aeroport;
    private int ordre;
    private LocalTime heureArrivee;
    private LocalTime heureDepart;

    public Escale() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVolId() { return volId; }
    public void setVolId(int volId) { this.volId = volId; }

    public Aeroport getAeroport() { return aeroport; }
    public void setAeroport(Aeroport aeroport) { this.aeroport = aeroport; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }

    public LocalTime getHeureArrivee() { return heureArrivee; }
    public void setHeureArrivee(LocalTime heureArrivee) { this.heureArrivee = heureArrivee; }

    public LocalTime getHeureDepart() { return heureDepart; }
    public void setHeureDepart(LocalTime heureDepart) { this.heureDepart = heureDepart; }
}

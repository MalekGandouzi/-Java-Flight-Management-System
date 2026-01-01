package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Vol {

    private int id;

    private Aeroport depart;
    private Aeroport arrivee;

    private LocalDate dateDepart;
    private LocalDate dateArrivee;

    private LocalTime heureDepart;
    private LocalTime heureArrivee;

    private boolean reservable;
    private List<Escale> escales;

    // ---------- ID ----------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ---------- AEROPORTS ----------
    public Aeroport getDepart() {
        return depart;
    }

    public void setDepart(Aeroport depart) {
        this.depart = depart;
    }

    public Aeroport getArrivee() {
        return arrivee;
    }

    public void setArrivee(Aeroport arrivee) {
        this.arrivee = arrivee;
    }

    // ---------- DATES ----------
    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    // ---------- HEURES ----------
    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    // ---------- RESERVATION ----------
    public boolean isReservable() {
        return reservable;
    }

    public void setReservable(boolean reservable) {
        this.reservable = reservable;
    }

    // ---------- ESCALES ----------
    public List<Escale> getEscales() {
        return escales;
    }

    public void setEscales(List<Escale> escales) {
        this.escales = escales;
    }

    public int getNombreEscales() {
        return escales == null ? 0 : escales.size();
    }
}

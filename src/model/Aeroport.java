/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Aeroport {
    private int id;
    private String nom;
    private Pays pays;

    public Aeroport() {}

    public Aeroport(int id, String nom, Pays pays) {
        this.id = id;
        this.nom = nom;
        this.pays = pays;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Pays getPays() { return pays; }
    public void setPays(Pays pays) { this.pays = pays; }

    @Override public String toString() { return nom; } // combo/list/table
}

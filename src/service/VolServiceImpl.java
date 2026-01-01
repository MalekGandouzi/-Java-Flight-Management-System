/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.VolDAO;
import model.Vol;
import java.util.Comparator;
import java.util.List;

public class VolServiceImpl implements VolService {

    private List<Vol> vols;

    public VolServiceImpl() {
        vols = new VolDAO().findAll();
    }

    @Override
    public Vol volMinEscales() {
        return vols.stream()
                .min(Comparator.comparingInt(Vol::getNombreEscales))
                .orElse(null);
    }

    @Override
    public Vol volMaxEscales() {
        return vols.stream()
                .max(Comparator.comparingInt(Vol::getNombreEscales))
                .orElse(null);
    }
}


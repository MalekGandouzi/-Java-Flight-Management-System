/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.UtilisateurDAO;

public class AuthService {

    public boolean login(String username, String password) {
        return new UtilisateurDAO().authentifier(username, password);
    }
}

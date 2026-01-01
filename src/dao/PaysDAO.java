/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Pays;
import util.DBConnection;

import java.sql.*;
import java.util.*;

public class PaysDAO {
    public List<Pays> findAll() {
        List<Pays> list = new ArrayList<>();
        String sql = "SELECT id, nom FROM pays ORDER BY nom";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Pays p = new Pays();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}


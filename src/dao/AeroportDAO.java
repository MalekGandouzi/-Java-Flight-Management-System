/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Aeroport;
import model.Pays;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AeroportDAO {

    public int insert(Aeroport a) {
        if (a == null || a.getPays() == null) return -1;

        String sql = "INSERT INTO aeroport(nom, pays_id) VALUES (?, ?)";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNom());
            ps.setInt(2, a.getPays().getId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    a.setId(id);
                    return id;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update(Aeroport a) {
        if (a == null || a.getPays() == null) return;

        String sql = "UPDATE aeroport SET nom = ?, pays_id = ? WHERE id = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, a.getNom());
            ps.setInt(2, a.getPays().getId());
            ps.setInt(3, a.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM aeroport WHERE id = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            // Si un vol utilise cet aeroport, MySQL va refuser (FK) => c’est normal
            e.printStackTrace();
        }
    }

    public Aeroport findById(int id) {
        String sql = """
            SELECT a.id, a.nom, p.id AS pid, p.nom AS pnom
            FROM aeroport a
            JOIN pays p ON a.pays_id = p.id
            WHERE a.id = ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pays p = new Pays();
                    p.setId(rs.getInt("pid"));
                    p.setNom(rs.getString("pnom"));

                    Aeroport a = new Aeroport();
                    a.setId(rs.getInt("id"));
                    a.setNom(rs.getString("nom"));
                    a.setPays(p);
                    return a;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Aeroport> findAll() {
        List<Aeroport> list = new ArrayList<>();

        String sql = """
          SELECT a.id, a.nom, p.id AS pid, p.nom AS pnom
          FROM aeroport a
          JOIN pays p ON a.pays_id = p.id
          ORDER BY a.id DESC
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Pays p = new Pays();
                p.setId(rs.getInt("pid"));
                p.setNom(rs.getString("pnom"));

                Aeroport a = new Aeroport();
                a.setId(rs.getInt("id"));
                a.setNom(rs.getString("nom"));
                a.setPays(p);

                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

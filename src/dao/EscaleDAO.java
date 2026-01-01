package dao;

import model.Aeroport;
import model.Escale;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EscaleDAO {

    public void insert(Escale e) {
        String sql = """
            INSERT INTO escale(vol_id, aeroport_id, ordre, heure_arrivee, heure_depart)
            VALUES (?,?,?,?,?)
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, e.getVolId());
            ps.setInt(2, e.getAeroport().getId());
            ps.setInt(3, e.getOrdre());
            ps.setTime(4, Time.valueOf(e.getHeureArrivee()));
            ps.setTime(5, Time.valueOf(e.getHeureDepart()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Escale> findAll() {
    List<Escale> list = new ArrayList<>();

    String sql = """
        SELECT e.id, e.vol_id, e.ordre, e.heure_arrivee, e.heure_depart,
               a.id AS aid, a.nom AS anom
        FROM escale e
        JOIN aeroport a ON e.aeroport_id = a.id
        ORDER BY e.vol_id DESC, e.ordre ASC
    """;

    try (Connection cn = DBConnection.getConnection();
         Statement st = cn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            Escale e = new Escale();
            e.setId(rs.getInt("id"));
            e.setVolId(rs.getInt("vol_id"));
            e.setOrdre(rs.getInt("ordre"));
            e.setHeureArrivee(rs.getTime("heure_arrivee").toLocalTime());
            e.setHeureDepart(rs.getTime("heure_depart").toLocalTime());

            Aeroport a = new Aeroport();
            a.setId(rs.getInt("aid"));
            a.setNom(rs.getString("anom"));
            e.setAeroport(a);

            list.add(e);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return list;
}



    public void delete(int id) {
        String sql = "DELETE FROM escale WHERE id = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getNextOrdre(int volId) {
        String sql = "SELECT COALESCE(MAX(ordre), 0) + 1 AS nextOrdre FROM escale WHERE vol_id = ?";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, volId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("nextOrdre");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    // ✅ Contrôle : ordre unique par vol
    public boolean ordreExists(int volId, int ordre) {
        String sql = "SELECT 1 FROM escale WHERE vol_id = ? AND ordre = ? LIMIT 1";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, volId);
            ps.setInt(2, ordre);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public java.util.List<Escale> findByVolId(int volId) {
    java.util.List<Escale> list = new java.util.ArrayList<>();

    String sql = """
        SELECT e.id, e.vol_id, e.ordre, e.heure_arrivee, e.heure_depart,
               a.id AS aid, a.nom AS anom
        FROM escale e
        JOIN aeroport a ON e.aeroport_id = a.id
        WHERE e.vol_id = ?
        ORDER BY e.ordre ASC
    """;

    try (Connection cn = DBConnection.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setInt(1, volId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Escale e = new Escale();
                e.setId(rs.getInt("id"));
                e.setVolId(rs.getInt("vol_id"));
                e.setOrdre(rs.getInt("ordre"));

                Time ha = rs.getTime("heure_arrivee");
                Time hd = rs.getTime("heure_depart");
                e.setHeureArrivee(ha == null ? null : ha.toLocalTime());
                e.setHeureDepart(hd == null ? null : hd.toLocalTime());

                Aeroport a = new Aeroport();
                a.setId(rs.getInt("aid"));
                a.setNom(rs.getString("anom"));
                e.setAeroport(a);

                list.add(e);
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return list;
}

}

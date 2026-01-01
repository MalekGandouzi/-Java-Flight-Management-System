package dao;

import model.Aeroport;
import model.Vol;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolDAO {

    public void insert(Vol v) {
        String sql = """
            INSERT INTO vol(
                aeroport_depart, aeroport_arrivee,
                date_depart, date_arrivee,
                heure_depart, heure_arrivee,
                reservable
            ) VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, v.getDepart().getId());
            ps.setInt(2, v.getArrivee().getId());

            ps.setDate(3, v.getDateDepart() == null ? null : Date.valueOf(v.getDateDepart()));
            ps.setDate(4, v.getDateArrivee() == null ? null : Date.valueOf(v.getDateArrivee()));

            ps.setTime(5, v.getHeureDepart() == null ? null : Time.valueOf(v.getHeureDepart()));
            ps.setTime(6, v.getHeureArrivee() == null ? null : Time.valueOf(v.getHeureArrivee()));

            ps.setBoolean(7, v.isReservable());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) v.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vol findById(int id) {
        String sql = """
            SELECT
                v.id,
                v.aeroport_depart AS depart_id,
                a1.nom            AS depart_nom,
                v.aeroport_arrivee AS arrivee_id,
                a2.nom             AS arrivee_nom,
                v.date_depart,
                v.date_arrivee,
                v.heure_depart,
                v.heure_arrivee,
                v.reservable
            FROM vol v
            JOIN aeroport a1 ON v.aeroport_depart = a1.id
            JOIN aeroport a2 ON v.aeroport_arrivee = a2.id
            WHERE v.id = ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Vol v = new Vol();
                v.setId(rs.getInt("id"));

                Aeroport depart = new Aeroport();
                depart.setId(rs.getInt("depart_id"));
                depart.setNom(rs.getString("depart_nom"));
                v.setDepart(depart);

                Aeroport arrivee = new Aeroport();
                arrivee.setId(rs.getInt("arrivee_id"));
                arrivee.setNom(rs.getString("arrivee_nom"));
                v.setArrivee(arrivee);

                Date dd = rs.getDate("date_depart");
                Date da = rs.getDate("date_arrivee");
                Time hd = rs.getTime("heure_depart");
                Time ha = rs.getTime("heure_arrivee");

                v.setDateDepart(dd == null ? null : dd.toLocalDate());
                v.setDateArrivee(da == null ? null : da.toLocalDate());
                v.setHeureDepart(hd == null ? null : hd.toLocalTime().withSecond(0).withNano(0));
                v.setHeureArrivee(ha == null ? null : ha.toLocalTime().withSecond(0).withNano(0));

                v.setReservable(rs.getBoolean("reservable"));
                return v;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Vol> findAll() {
        List<Vol> list = new ArrayList<>();

        String sql = """
            SELECT
                v.id,
                v.aeroport_depart AS depart_id,
                a1.nom            AS depart_nom,
                v.aeroport_arrivee AS arrivee_id,
                a2.nom             AS arrivee_nom,
                v.date_depart,
                v.date_arrivee,
                v.heure_depart,
                v.heure_arrivee,
                v.reservable
            FROM vol v
            JOIN aeroport a1 ON v.aeroport_depart = a1.id
            JOIN aeroport a2 ON v.aeroport_arrivee = a2.id
            ORDER BY v.id DESC
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vol v = new Vol();
                v.setId(rs.getInt("id"));

                Aeroport depart = new Aeroport();
                depart.setId(rs.getInt("depart_id"));
                depart.setNom(rs.getString("depart_nom"));
                v.setDepart(depart);

                Aeroport arrivee = new Aeroport();
                arrivee.setId(rs.getInt("arrivee_id"));
                arrivee.setNom(rs.getString("arrivee_nom"));
                v.setArrivee(arrivee);

                Date dd = rs.getDate("date_depart");
                Date da = rs.getDate("date_arrivee");
                Time hd = rs.getTime("heure_depart");
                Time ha = rs.getTime("heure_arrivee");

                v.setDateDepart(dd == null ? null : dd.toLocalDate());
                v.setDateArrivee(da == null ? null : da.toLocalDate());
                v.setHeureDepart(hd == null ? null : hd.toLocalTime().withSecond(0).withNano(0));
                v.setHeureArrivee(ha == null ? null : ha.toLocalTime().withSecond(0).withNano(0));

                v.setReservable(rs.getBoolean("reservable"));

                list.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void update(Vol v) {
        String sql = """
            UPDATE vol SET
                aeroport_depart = ?,
                aeroport_arrivee = ?,
                date_depart = ?,
                date_arrivee = ?,
                heure_depart = ?,
                heure_arrivee = ?,
                reservable = ?
            WHERE id = ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, v.getDepart().getId());
            ps.setInt(2, v.getArrivee().getId());

            ps.setDate(3, v.getDateDepart() == null ? null : Date.valueOf(v.getDateDepart()));
            ps.setDate(4, v.getDateArrivee() == null ? null : Date.valueOf(v.getDateArrivee()));

            ps.setTime(5, v.getHeureDepart() == null ? null : Time.valueOf(v.getHeureDepart()));
            ps.setTime(6, v.getHeureArrivee() == null ? null : Time.valueOf(v.getHeureArrivee()));

            ps.setBoolean(7, v.isReservable());
            ps.setInt(8, v.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM vol WHERE id = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Vol findMinEscales() {
    String sql = """
        SELECT v.id,
               a1.id AS depart_id, a1.nom AS depart_nom,
               a2.id AS arrivee_id, a2.nom AS arrivee_nom,
               v.date_depart, v.date_arrivee, v.heure_depart, v.heure_arrivee, v.reservable,
               COUNT(e.id) AS nb
        FROM vol v
        JOIN aeroport a1 ON v.aeroport_depart = a1.id
        JOIN aeroport a2 ON v.aeroport_arrivee = a2.id
        LEFT JOIN escale e ON e.vol_id = v.id
        GROUP BY v.id
        ORDER BY nb ASC, v.id ASC
        LIMIT 1
    """;
    return findOneFromStatsQuery(sql);
}

public Vol findMaxEscales() {
    String sql = """
        SELECT v.id,
               a1.id AS depart_id, a1.nom AS depart_nom,
               a2.id AS arrivee_id, a2.nom AS arrivee_nom,
               v.date_depart, v.date_arrivee, v.heure_depart, v.heure_arrivee, v.reservable,
               COUNT(e.id) AS nb
        FROM vol v
        JOIN aeroport a1 ON v.aeroport_depart = a1.id
        JOIN aeroport a2 ON v.aeroport_arrivee = a2.id
        LEFT JOIN escale e ON e.vol_id = v.id
        GROUP BY v.id
        ORDER BY nb DESC, v.id ASC
        LIMIT 1
    """;
    return findOneFromStatsQuery(sql);
}

private Vol findOneFromStatsQuery(String sql) {
    try (Connection cn = DBConnection.getConnection();
         Statement st = cn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        if (!rs.next()) return null;

        Vol v = new Vol();
        v.setId(rs.getInt("id"));

        Aeroport depart = new Aeroport();
        depart.setId(rs.getInt("depart_id"));
        depart.setNom(rs.getString("depart_nom"));
        v.setDepart(depart);

        Aeroport arrivee = new Aeroport();
        arrivee.setId(rs.getInt("arrivee_id"));
        arrivee.setNom(rs.getString("arrivee_nom"));
        v.setArrivee(arrivee);

        Date dd = rs.getDate("date_depart");
        Date da = rs.getDate("date_arrivee");
        Time hd = rs.getTime("heure_depart");
        Time ha = rs.getTime("heure_arrivee");

        v.setDateDepart(dd == null ? null : dd.toLocalDate());
        v.setDateArrivee(da == null ? null : da.toLocalDate());
        v.setHeureDepart(hd == null ? null : hd.toLocalTime().withSecond(0).withNano(0));
        v.setHeureArrivee(ha == null ? null : ha.toLocalTime().withSecond(0).withNano(0));

        v.setReservable(rs.getBoolean("reservable"));
        return v;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

}

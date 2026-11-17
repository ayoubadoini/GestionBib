package com.example.gestionbib.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BibUtil {

    private static final String URL = "jdbc:sqlite:C:/Users/ayoub/IdeaProjects/GestionBib/bib.db";
    private static String dernierTitreErreur = "";
    private static String dernierMessageErreur = "";

    public static String getDernierTitreErreur() { return dernierTitreErreur; }
    public static String getDernierMessageErreur() { return dernierMessageErreur; }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Créer la table si elle n'existe pas
    public static void initialiserDB() {
        String sql = "CREATE TABLE IF NOT EXISTS livre (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titre TEXT," +
                "couleur TEXT," +
                "nbPages INTEGER);";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Base de données initialisée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur init DB: " + e.getMessage());
        }
    }

    // 1. Récupérer tous les livres
    public static ObservableList<Livre> getLivres() {
        ObservableList<Livre> livres = FXCollections.observableArrayList();
        String sql = "Select * from livre order by id;";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livres.add(new Livre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("couleur"),
                        rs.getInt("nbPages")
                ));
            }
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur getLivres";
            dernierMessageErreur = e.getMessage();
        }
        return livres;
    }

    // 2. Récupérer un livre par ID
    public static Livre getLivreById(int id) {
        String sql = "Select * from livre where id = ?;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Livre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("couleur"),
                        rs.getInt("nbPages")
                );
            }
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur getLivreById";
            dernierMessageErreur = e.getMessage();
        }
        return null;
    }

    // 3. Ajouter un livre
    public static boolean ajouterLivre(String titre, String couleur, int nbPage) {
        String sql = "Insert into livre(titre,couleur,nbPages) values (?,?,?);";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titre);
            pstmt.setString(2, couleur);
            pstmt.setInt(3, nbPage);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur ajouterLivre";
            dernierMessageErreur = e.getMessage();
            return false;
        }
    }

    // 4. Modifier un livre
    public static boolean modifierLivre(int id, String titre, String couleur, int nbPage) {
        String sql = "Update Livre set titre=?,couleur=?,nbPages=? where id=?;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titre);
            pstmt.setString(2, couleur);
            pstmt.setInt(3, nbPage);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur modifierLivre";
            dernierMessageErreur = e.getMessage();
            return false;
        }
    }

    // 5. Rechercher des livres
    public static ObservableList<Livre> rechercherLivres(String titreR) {
        ObservableList<Livre> livres = FXCollections.observableArrayList();
        String sql = "Select * from livre where titre like ? order by id;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titreR + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                livres.add(new Livre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("couleur"),
                        rs.getInt("nbPages")
                ));
            }
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur rechercherLivres";
            dernierMessageErreur = e.getMessage();
        }
        return livres;
    }

    // 6. Supprimer tous les livres
    public static boolean supprimerTout() {
        String sql = "delete from livre;";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur supprimerTout";
            dernierMessageErreur = e.getMessage();
            return false;
        }
    }

    // 7. Supprimer un livre par id
    public static boolean supprimerLivre(int id) {
        String sql = "delete from livre where id= ?;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            dernierTitreErreur = "Erreur supprimerLivre";
            dernierMessageErreur = e.getMessage();
            return false;
        }
    }
}
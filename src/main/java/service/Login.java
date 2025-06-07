/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.*;
import users.Player;
import users.Role;

/**
 * Provides functionality for user login, registration, and username availability checks.
 * This class interacts with the database to facilitate user authentication
 * and management processes.
 *
 * @author Gruppo6
 */
public class Login {

    /**
     * Authenticates a user by validating the provided username and password against the database.
     * If the credentials are valid, returns a {@link Player} object containing user details.
     *
     * @param inputUsername the username of the user attempting to log in
     * @param inputPassword the password of the user attempting to log in
     * @return a {@link Player} object if authentication is successful, or {@code null} if the credentials are invalid or an error occurs
     */

    public static Player login(String inputUsername, String inputPassword) {
        String sql = "SELECT username, password, role FROM users WHERE username = ? AND password = ?";

        try(Connection conn = DatabaseManagement.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, inputUsername);
            pstmt.setString(2, inputPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String roleStr = rs.getString("role");

                Role role = Role.valueOf(roleStr.toUpperCase());

                return new Player(username, password, role);
            } else {
                System.out.println("Credenziali non valide.");
            }

        } catch (SQLException e) {
            System.out.println("Errore di connessione o query su login: " + e.getMessage());
        }

        return null;
    }

    /**
     * Checks if a given username already exists in the database.
     *
     * @param inputUsername the username to check for existence in the database
     * @return {@code true} if the username is already taken, {@code false} otherwise or in case of an error
     */
    public static boolean isUsernameTaken(String inputUsername) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try(Connection conn = DatabaseManagement.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUsername);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Errore di connessione o query su controllo username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Registers a new user by adding their username, password, and role to the database.
     * Uses an SQLite database for storage.
     *
     * @param inputUsername the username of the new user
     * @param inputPassword the password of the new user
     * @param role the role of the new user, specified as a {@link Role} enum
     */
    public static void userRegister(String inputUsername, String inputPassword, Role role) {
        String url = "jdbc:sqlite:wordageddon.db"; // Percorso relativo al DB
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try(Connection conn = DatabaseManagement.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);){


            stmt.setString(1, inputUsername);
            stmt.setString(2, inputPassword);
            stmt.setString(3, role.toString());

            stmt.executeUpdate();
            System.out.println("Registrazione avvenuta con successo.");
        }
        catch(SQLException e)
        {
            System.out.println("Errore di connessione o query su registrazione: " + e.getMessage());
        }
    }
}
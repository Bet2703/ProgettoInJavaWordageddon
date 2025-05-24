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
 * Provides login functionality by verifying user credentials against the database.
 * Connects to a local SQLite database to retrieve user data and instantiate a {@link Player} if authentication succeeds.
 * 
 * <p>Database: {@code wordageddon.db} <br>
 * Table: {@code users} with columns {@code username}, {@code password}, {@code role}</p>
 * 
 * @author Gruppo6
 */
public class Login {

    /**
     * Attempts to log in a user by checking the provided username and password against the database.
     *
     * @param inputUsername the username entered by the user
     * @param inputPassword the password entered by the user
     * @return a {@link Player} object if the login is successful; {@code null} otherwise
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
     * Checks if the provided username is already taken in the database.
     *
     * @param inputUsername the username entered by the user
     * @return a boolean indicating whether the username is already taken or not
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
     * Registers a new user in the database after checking if username is already taken.
     *
     * @param inputUsername the username entered by the user
     * @param inputPassword the password entered by the user
     * @param role the role of the user (either {@link Role#BASE} or {@link Role#ADMIN})
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
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
 *
 * @author Gruppo6
 */
public class Login {
     public static Player login(String inputUsername, String inputPassword) {
        String url = "jdbc:sqlite:wordageddon.db"; // Percorso relativo al DB
        String sql = "SELECT username, password, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
            System.out.println("Errore di connessione o query: " + e.getMessage());
        }

        return null;
    }
}

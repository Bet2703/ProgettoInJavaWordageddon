/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import users.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Benedetta
 */
public class DatabaseManagement {
    
    private static final String URL="jdbc:sqlite:./wordageddon.db";
    private static Connection connection = null;

    /**
     * Creates a connection with the database wordageddon.db
     *
     * @return a {@link Connection} object if the connection is successful; {@code null} otherwise
     */

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                System.out.println("Connessione al database.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nella connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }


    /**
     * Closes the connection with the database wordageddon.db
     *
     */

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connessione al database chiusa.");
                }
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }

    /*NEL MAIN
        Connection conn = DatabaseManagement.getConnection();
        if(conn != null){
            System.out.println("connesso");
        }
    */
       
    /*public static void inserisciUtente(String username, String password) {
        String sql = "INSERT INTO User (username, password, is_admin) VALUES (?, ?, 0)";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int righeInserite = pstmt.executeUpdate();
            if (righeInserite > 0) {
                System.out.println("Utente inserito correttamente.");
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento dell'utente: " + e.getMessage());
        }
    }*/
}

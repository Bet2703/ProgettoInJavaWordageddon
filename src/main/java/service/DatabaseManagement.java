/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Benedetta
 */
public class DatabaseManagement {
    
    private static final String URL="jbdc:sqlite:./wordageddon.db";
    private static Connection connection = null;
    
    public static Connection getConnection(){
        if(connection == null){
            try{
                Class.forName("org.sqlite.JBDC");
                connection = DriverManager.getConnection(URL);
                System.out.println("Connessione al database riuscita");
            }catch(ClassNotFoundException | SQLException e){
                System.err.println("Errore durante la connessione al database: " + e.getMessage());
            }
        }
        return connection;
    }
    
    public static void closeConnection(){
        if(connection != null){
            try{
                connection.close();
                System.out.println("Connessione chiusa.");
            }catch(SQLException e){
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        } 
    }
}

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
 * Provides functionality for managing an SQLite database connection.
 * Facilitates establishing and closing a connection to the database file {@code wordageddon.db}.
 *
 * @author Gruppo6
 */
public class DatabaseManagement {

    /**
     * The URL representing the database connection string for an SQLite database.
     * This constant is used to specify the path of the SQLite database file "wordageddon.db".
     * The value is set to use a relative path in conjunction with the JDBC SQLite driver.
     *
     * Format: jdbc:sqlite:<relative_path_to_db_file>
     *
     * Example Value: "jdbc:sqlite:./wordageddon.db"
     */
    private static final String URL="jdbc:sqlite:./wordageddon.db";

    /**
     * Represents the single shared connection instance used for interactions
     * with the SQLite database {@code wordageddon.db}. This variable is initialized
     * and managed within the {@code DatabaseManagement} class.
     *
     * The {@code connection} is lazy-initialized and reused throughout the application lifecycle,
     * ensuring efficient resource utilization and avoiding multiple instances per database connection.
     *
     * It is set to {@code null} initially and should only be accessed or modified
     * through the appropriate methods within the {@code DatabaseManagement} class,
     * such as {@link DatabaseManagement#getConnection()} and {@link DatabaseManagement#closeConnection()}.
     */
    private static Connection connection = null;

    /**
     * Establishes a connection to the SQLite database specified by the {@code URL}.
     * If the connection object is null or already closed, it initializes a new connection.
     * The connection is managed as a singleton to ensure efficient resource usage.
     *
     * @return a {@link Connection} object representing the connection to the database,
     *         or {@code null} if an error occurred during the connection process.
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
     * Closes the active connection to the SQLite database if it exists and is open.
     * This method ensures that any established connection to the database is properly closed,
     * releasing associated resources.
     *
     * If the connection is already closed or null, the method does nothing.
     * If an error occurs during the closure, it logs the error message to the standard error stream.
     *
     * Even if useless with the use of try-with-resources, this method is implemented anyway.
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
}

package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    private final String[] createTables = {
    """
    CREATE TABLE IF NOT EXISTS user (
      `username` varchar(256) NOT NULL,
      `password` varchar(256) NOT NULL,
      `email` varchar(256) NOT NULL,
      PRIMARY KEY (`username`),
      INDEX (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """,
    """
    CREATE TABLE IF NOT EXISTS game (
      `gameID` int AUTO_INCREMENT,
      `whiteUsername` varchar(256),
      `blackUsername` varchar(256),
      `game_name` varchar(256),
      `game_data` TEXT,
      PRIMARY KEY (`gameID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """,
    """
    CREATE TABLE IF NOT EXISTS auth (
      `authToken` varchar(256) NOT NULL,
      `username` varchar(256) NOT NULL,
      PRIMARY KEY (`authToken`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
    };

    //Load the database information for the db.properties file.
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    //adds tables to database
    public void configureDatabase() throws DataAccessException {
        createDatabase();  // Ensure the database exists

        try (var conn = getConnection()) {
            // Iterate over the createTable SQL statements and execute them
            for (var statement : createTables) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database: " + e.getMessage());
        }
    }

    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
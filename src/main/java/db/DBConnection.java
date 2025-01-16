package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/dulmark_clothing";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    private Connection connection = null;
    private static DBConnection instance;

    private DBConnection() { }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static DBConnection getInstance() {
        return null == instance ? instance = new DBConnection() : instance;
    }
}

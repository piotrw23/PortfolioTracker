package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:portfolio.db";

    public void initializeDatabase() {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ticker TEXT NOT NULL," +
                    "shares INTEGER NOT NULL," +
                    "price TEXT NOT NULL);";

            stmt.execute(sql);
            System.out.println("Database created.");
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

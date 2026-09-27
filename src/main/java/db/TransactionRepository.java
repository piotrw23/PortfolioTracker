package db;

import model.Transaction;

import java.sql.*;

public class TransactionRepository {
    private final DatabaseManager db;

    public TransactionRepository(DatabaseManager db) {
        this.db = db;
    }

    public Transaction save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions(ticker, shares, price) VALUES (?, ?, ?)";

        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, transaction.ticker());
            stmt.setInt(2, transaction.shares());
            stmt.setString(3, transaction.price().toPlainString());
            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    long id = rs.getLong(1);
                    return new Transaction(id, transaction.ticker(), transaction.shares(), transaction.price());
                }
                throw new SQLException("Failed to read new transaction id");
            }
        }
    }
}

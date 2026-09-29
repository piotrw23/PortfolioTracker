package db;

import model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Transaction> findAll() throws SQLException {
        String sql = "SELECT * FROM transactions";
        List<Transaction> transactions = new ArrayList<>();

        try(Connection conn = db.getConnection();
            Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                transactions.add(new Transaction(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getInt(3),
                        new BigDecimal(rs.getString(4))
                ));
            }
            return transactions;
        }
    }
}

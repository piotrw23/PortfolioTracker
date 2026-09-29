import command.AddCommand;
import db.DatabaseManager;
import db.TransactionRepository;
import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCommandTest {
    @TempDir
    Path tempDir;

    private CommandLine cli;
    private TransactionRepository repo;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        DatabaseManager db = new DatabaseManager(tempDir.resolve("portfolio.db"));
        db.initializeDatabase();
        this.repo = new TransactionRepository(db);
        this.cli = new CommandLine(new AddCommand(repo));
    }

    @Test
    public void acceptsValidTransaction() {
        int exitCode = cli.execute("AAPL", "2", "150.67");
        assertEquals(0, exitCode);
    }

    @Test
    public void rejectsZeroShares() {
        int exitCode = cli.execute("AAPL", "0", "150.67");
        assertEquals(2, exitCode);
    }

    @Test
    public void rejectsNonNumericPrice() {
        int exitCode = cli.execute("AAPL", "3", "twenty");
        assertEquals(2, exitCode);
    }

    @Test
    public void rejectsEmptyTransaction() {
        int exitCode = cli.execute("AAPL", "", "");
        assertEquals(2, exitCode);
    }

    @Test
    public void savesTickerInUpperCase() throws SQLException {
        int exitCode = cli.execute("aapl", "2", "150.67");
        assertEquals(0, exitCode);

        List<Transaction> transactions = repo.findAll();
        assertEquals(1, transactions.size());
        assertEquals("AAPL", transactions.get(0).ticker());
    }
}

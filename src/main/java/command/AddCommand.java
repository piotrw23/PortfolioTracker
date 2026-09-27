package command;

import db.TransactionRepository;
import model.Transaction;
import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParameterException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.Callable;

@Command(
        name = "add",
        description = "Adding new transaction to the portfolio."
)
public class AddCommand implements Callable<Integer> {
    @Spec
    private CommandLine.Model.CommandSpec spec;

    @Parameters(index = "0", description = "Share's ticker, e.g. APPL")
    private String ticker;

    @Parameters(index = "1", description = "Number of shares")
    private int shares;

    @Parameters(index = "2", description = "Share price")
    private BigDecimal price;

    private TransactionRepository repository;
    public AddCommand(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer call() throws Exception {
        Transaction t = new Transaction(null, ticker.toUpperCase(), shares, price);
        validate();

        try {
            Transaction saved = repository.save(t);
            System.out.println("Saved transaction #" + saved.id());
            return 0;
        } catch (SQLException e) {
            System.err.println("Failed to save transaction: " + e.getMessage());
            return 1;
        }
    }

    private void validate() throws ParameterException {
        if(shares <= 0) {
            throw new ParameterException(spec.commandLine(), "Number of shares must be a positive integer");
        }

        if(price.signum() <= 0) {
            throw new ParameterException(spec.commandLine(), "Price must be a positive number");
        }

        if(ticker == null || ticker.isEmpty()) {
            throw new ParameterException(spec.commandLine(), "Ticker must not be empty");
        }
    }
}

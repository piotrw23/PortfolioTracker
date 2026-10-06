package command;

import db.TransactionRepository;
import model.Position;
import model.Transaction;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine;
import price.FakePriceProvider;
import print.PrintPositionStats;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "status",
        description = "Reading ticker and printing total profit/loss from transactions involving given ticker"
)
public class StatusCommand implements Callable<Integer> {
    TransactionRepository repo;

    public StatusCommand(TransactionRepository repo) {
        this.repo = repo;
    }

    @Spec
    CommandLine.Model.CommandSpec spec;

    @Parameters(index = "0", description = "Share's ticker, e.g. APPL")
    private String ticker;

    @Override
    public Integer call() throws SQLException {
        List<Transaction> tickerTransactions = repo.findByTicker(ticker);

        Position position = new Position(ticker, tickerTransactions);
        BigDecimal currentPrice = new FakePriceProvider(ticker).getPrice();

        PrintPositionStats positionStats = new PrintPositionStats(
                new String[] {"Ticker", "Total shares", "Avg cost", "Current price", "Total profit", "Percent"},
                position,
                currentPrice);

        positionStats.printTable();

        return 0;
    }
}

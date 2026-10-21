package command;

import db.TransactionRepository;
import model.Position;
import model.Transaction;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine;
import price.FakePriceProvider;
import price.PriceProvider;
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
    private final TransactionRepository repo;
    private final PriceProvider priceProvider;

    public StatusCommand(TransactionRepository repo,  PriceProvider priceProvider) {
        this.repo = repo;
        this.priceProvider = priceProvider;
    }

    @Spec
    CommandLine.Model.CommandSpec spec;

    @Parameters(index = "0", description = "Share's ticker, e.g. APPL")
    private String ticker;

    @Override
    public Integer call() throws SQLException {
        List<Transaction> tickerTransactions = repo.findByTicker(ticker);

        Position position = new Position(ticker, tickerTransactions);
        BigDecimal currentPrice = priceProvider.getPrice(ticker);

        PrintPositionStats positionStats = new PrintPositionStats(
                new String[] {"Ticker", "Total shares", "Avg cost", "Current price", "Total profit", "Percent"},
                position,
                currentPrice);

        positionStats.printTable();

        return 0;
    }
}

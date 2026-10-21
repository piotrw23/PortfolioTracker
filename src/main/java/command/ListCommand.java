package command;

import db.TransactionRepository;
import model.Transaction;
import picocli.CommandLine.Command;
import print.PrintTransactionsTable;

import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "list",
        description = "Prints transactions table."
)
public class ListCommand implements Callable<Integer> {
    private final TransactionRepository repository;

    public ListCommand(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer call() throws Exception {
        List<Transaction> transactions = repository.findAll();

        if(transactions.isEmpty()){
            System.out.println("No transactions found");
        }

        PrintTransactionsTable table = new PrintTransactionsTable(new String[]{"ID", "Ticker", "Shares", "Price", "Value"},
                                        transactions);

        table.printTable();

        return 0;
    }
}

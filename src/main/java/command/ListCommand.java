package command;

import db.TransactionRepository;
import print.PrintTable;
import model.Transaction;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        PrintTable.printTable(transactions);

        return 0;
    }
}

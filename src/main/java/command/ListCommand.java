package command;

import db.TransactionRepository;
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

        printTable(transactions);

        return 0;
    }

    private void printTable(List<Transaction> transactions) {
        String[] headers = {"ID", "Ticker", "Shares", "Price", "Value"};

        // convert transactions to rows of text
        List<String[]> rows = new ArrayList<>();
        for(Transaction t : transactions) {
            BigDecimal value = t.price().multiply(BigDecimal.valueOf(t.shares()));
            rows.add(new String[] {
                    String.valueOf(t.id()),
                    t.ticker(),
                    String.valueOf(t.shares()),
                    t.price().toPlainString(),
                    value.toPlainString()
            });
        }

        // find max width for each column
        int[] widths = new int[headers.length];
        for(int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
            for(String[] row : rows) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        // print
        printRow(headers, widths);
        printSeparator(widths);
        for(String[] row : rows) {
            printRow(row, widths);
        }
    }

    private void printRow(String[] cells, int[] widths) {
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < cells.length; i++) {
            if(i > 0) {
                line.append(" | ");
            }
            String format = (i == 1) ? "%-" + widths[i] + "s" : "%" + widths[i] + "s";
            line.append(String.format(format, cells[i]));
        }
        System.out.println(line);
    }

    private void printSeparator(int[] widths) {
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < widths.length; i++) {
            if(i > 0) {
                line.append("-+-");
            }
            line.append("-".repeat(widths[i]));
        }
        System.out.println(line);
    }
}

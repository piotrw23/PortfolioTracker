package print;

import model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintTransactionsTable extends PrintTable {
    private final List<Transaction> transactions;

    public PrintTransactionsTable(String[] headers, List<Transaction> transactions) {
        super(headers);
        this.transactions = transactions;
    }
    @Override
     public void printTable() {

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
        int[] widths = findMaxWidths(rows, headers);

        // print
        printRow(headers, widths);
        printSeparator(widths);
        for(String[] row : rows) {
            printRow(row, widths);
        }
    }
}

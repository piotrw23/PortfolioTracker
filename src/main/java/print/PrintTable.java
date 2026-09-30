package print;

import model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface PrintTable {

     static void printTable(List<Transaction> transactions) {
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

    private static void printRow(String[] cells, int[] widths) {
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

    private static void printSeparator(int[] widths) {
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

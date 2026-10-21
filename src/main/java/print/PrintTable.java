package print;

import java.util.List;

public abstract class PrintTable {
    protected final String[] headers;

    protected PrintTable(String[] headers) {
        this.headers = headers;
    }

    protected abstract void printTable();

    protected static void printRow(String[] cells, int[] widths) {
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

    protected static void printSeparator(int[] widths) {
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < widths.length; i++) {
            if(i > 0) {
                line.append("-+-");
            }
            line.append("-".repeat(widths[i]));
        }
        System.out.println(line);
    }

    protected static int[] findMaxWidths(List<String[]> rows, String[] headers) {
        int[] widths = new int[headers.length];
        for(int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
            for(String[] row : rows) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }
        return widths;
    }
}

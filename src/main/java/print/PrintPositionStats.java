package print;

import model.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PrintPositionStats extends PrintTable {
    private Position position;
    private BigDecimal currentPrice;

    public PrintPositionStats(String[] headers, Position position, BigDecimal currentPrice) {
        super(headers);
        this.position = position;
        this.currentPrice = currentPrice;
    }

    @Override
    public void printTable() {
        List<String[]> rows = new ArrayList<>();

        String[] row = new String[] {
            position.ticker(),
            new Integer(position.getTotalShares()).toString(),
            position.getAvgPrice().setScale(2, RoundingMode.HALF_UP).toPlainString(),
            currentPrice.setScale(2, RoundingMode.HALF_UP).toPlainString(),
            position.getProfit(currentPrice).setScale(2, RoundingMode.HALF_UP).toString(),
            position.getPercentageProfit(currentPrice).setScale(2, RoundingMode.HALF_UP).toString(),
        };
        rows.add(row);

        int[] widths = findMaxWidths(rows, headers);

        printRow(headers, widths);
        printSeparator(widths);
        printRow(row, widths);
    }

}

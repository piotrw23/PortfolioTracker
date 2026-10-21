package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public record Position(String ticker, List<Transaction> transactions) {

    public Position {
        transactions = List.copyOf(transactions);
    }

    public int getTotalShares() {
        int shares = 0;
        for (Transaction transaction : transactions) {
            shares += transaction.shares();
        }
        return shares;
    }

    public BigDecimal getTotalCost() {
        BigDecimal cost = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            cost = cost.add(transaction.price().multiply(BigDecimal.valueOf(transaction.shares())));
        }
        return cost;
    }

    public BigDecimal getAvgPrice() {
        int totalShares = getTotalShares();
        if (totalShares == 0) {
            throw new IllegalStateException("No shares in position. Unable to compute average price");
        }
        return getTotalCost().divide(BigDecimal.valueOf(totalShares), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getProfit(BigDecimal currentPrice) {
        BigDecimal currentValue = currentPrice.multiply(BigDecimal.valueOf(getTotalShares()));
        return currentValue.subtract(getTotalCost());
    }

    public BigDecimal getPercentageProfit(BigDecimal currentPrice) {
        BigDecimal totalCost = getTotalCost();
        if (totalCost.signum() == 0) {
            throw new IllegalStateException("No cost in position. Unable to compute percentage profit");
        }
        return getProfit(currentPrice)
                .multiply(BigDecimal.valueOf(100))
                .divide(totalCost, 2, RoundingMode.HALF_UP);
    }
}
import model.Position;
import model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PositionTest {

    // Pozycja z dwoma zakupami o różnych cenach i różnej liczbie akcji:
    // 10 × 100.00 + 5 × 200.00 = 2000.00, łącznie 15 akcji
    private static Position twoTransactionPosition() {
        return new Position("AAPL", List.of(
                transaction(10, "100.00"),
                transaction(5, "200.00")
        ));
    }

    private static Transaction transaction(int shares, String price) {
        return new Transaction(null, "AAPL", shares, new BigDecimal(price));
    }

    // Porównanie wartości liczbowej, bez względu na skalę (2000 == 2000.00)
    private static void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
                () -> "expected " + expected + " but was " + actual.toPlainString());
    }

    // --- getTotalShares ---

    @Test
    void totalSharesSumsAllTransactions() {
        assertEquals(15, twoTransactionPosition().getTotalShares());
    }

    @Test
    void totalSharesOfEmptyPositionIsZero() {
        assertEquals(0, new Position("AAPL", List.of()).getTotalShares());
    }

    // --- getTotalCost ---

    @Test
    void totalCostMultipliesPriceByShares() {
        assertAmount("2000", twoTransactionPosition().getTotalCost());
    }

    @Test
    void totalCostOfEmptyPositionIsZero() {
        assertAmount("0", new Position("AAPL", List.of()).getTotalCost());
    }

    // --- getAvgPrice ---

    @Test
    void avgPriceIsWeightedByShares() {
        // 2000 / 15 = 133.333... (zwykła średnia cen dałaby 150)
        assertAmount("133.33", twoTransactionPosition().getAvgPrice());
    }

    @Test
    void avgPriceOfSingleTransactionEqualsItsPrice() {
        Position position = new Position("AAPL", List.of(transaction(7, "123.45")));
        assertAmount("123.45", position.getAvgPrice());
    }

    @Test
    void avgPriceIsRoundedHalfUp() {
        // (10 × 100 + 20 × 101) / 30 = 3020 / 30 = 100.666... -> 100.67
        Position position = new Position("AAPL", List.of(
                transaction(10, "100"),
                transaction(20, "101")
        ));
        assertAmount("100.67", position.getAvgPrice());
    }

    @Test
    void avgPriceOfEmptyPositionThrows() {
        Position position = new Position("AAPL", List.of());
        assertThrows(IllegalStateException.class, position::getAvgPrice);
    }

    // --- getProfit ---

    @Test
    void profitWhenPriceRose() {
        // 15 × 150 - 2000 = 250
        assertAmount("250", twoTransactionPosition().getProfit(new BigDecimal("150")));
    }

    @Test
    void lossWhenPriceFell() {
        // 15 × 120 - 2000 = -200
        assertAmount("-200", twoTransactionPosition().getProfit(new BigDecimal("120")));
    }

    @Test
    void zeroProfitAtAveragePrice() {
        Position position = new Position("AAPL", List.of(transaction(4, "50")));
        assertAmount("0", position.getProfit(new BigDecimal("50")));
    }

    // --- getPercentageProfit ---

    @Test
    void percentageProfitWhenPriceRose() {
        // 250 / 2000 = 12.5%
        assertAmount("12.50", twoTransactionPosition().getPercentageProfit(new BigDecimal("150")));
    }

    @Test
    void percentageLossWhenPriceFell() {
        // -200 / 2000 = -10%
        assertAmount("-10.00", twoTransactionPosition().getPercentageProfit(new BigDecimal("120")));
    }

    // --- niemutowalność (wymaga List.copyOf w konstruktorze kompaktowym) ---

    @Test
    void positionIsNotAffectedByChangesToSourceList() {
        List<Transaction> source = new ArrayList<>();
        source.add(transaction(10, "100"));
        Position position = new Position("AAPL", source);

        source.add(transaction(5, "200"));

        assertEquals(10, position.getTotalShares());
    }
}
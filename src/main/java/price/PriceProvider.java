package price;

import model.Transaction;

import java.math.BigDecimal;

public interface PriceProvider {
    BigDecimal getPrice(String ticker);
}

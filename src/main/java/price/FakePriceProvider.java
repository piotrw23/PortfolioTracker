package price;

import model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class FakePriceProvider implements PriceProvider {

    @Override
    public  BigDecimal getPrice(String ticker) {
        Random random = new Random();
         BigDecimal price = new BigDecimal(random.nextDouble(200, 300));

         return price;
    }
}

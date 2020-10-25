import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

public class Finance {

    public static BigDecimal encontrarPrecioApi(String ticker){
        try {
            Stock stock = YahooFinance.get(ticker);
            BigDecimal price = stock.getQuote().getPrice();
            return price;
        }catch(Exception e){
            return new BigDecimal(0);
        }
    }

}

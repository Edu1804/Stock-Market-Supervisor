import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

/*
This class have been created ir order to put everything related with the YahooFinance API
 */
public class Finance {

    //It founds the price of a stock with its Ticker
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

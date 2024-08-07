
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import com.crio.warmup.stock.PortfolioManagerApplication;
import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {


  private static RestTemplate restTemplate;


  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  public String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    String URL = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s", trade.getSymbol(), trade.getPurchaseDate(), endDate, token);
    System.out.println(URL);
    return URL;
  }

  public Double getOpeningPriceOnStartDate(List<Candle> candles) {
    Candle startDay = candles.get(0);
    return startDay.getOpen();
  }


  public Double getClosingPriceOnEndDate(List<Candle> candles) {
    Candle endDay = candles.get(candles.size() - 1);
    return endDay.getClose();
  }


  public static TiingoCandle[] getSerialisedObjects(String URL) {
    RestTemplate restTemplate = new RestTemplate();
    TiingoCandle[] tiingoCandles = restTemplate.getForObject(URL, TiingoCandle[].class);
    return tiingoCandles;
  }

  public List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    List<Candle> candlesList = new ArrayList<>();
    String URL = prepareUrl(trade, endDate, token);
    TiingoCandle[] tiingoCandles = getSerialisedObjects(URL);
    for (TiingoCandle tiingoCandle : tiingoCandles) {
      candlesList.add(tiingoCandle);
    }
    return candlesList;
  }

  public long getPeriod(LocalDate startDate, LocalDate endDate) {
    long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    return daysBetween;
  }

  public AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        LocalDate startDate = trade.getPurchaseDate();
        double totalNumDays = getPeriod(startDate, endDate);
        double totalReturns = ((sellPrice - buyPrice) * 1.0) / (buyPrice * 1.0);
        double annualizedReturns = Math.pow((1 + (totalReturns * 1.0)), (365 / (totalNumDays * 1.0))) - 1;
      return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturns);
  }

  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, LocalDate endDate) {
    List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
    for (PortfolioTrade portfolioTrade : portfolioTrades) {
      double buyPrice = 0;
      double sellPrice = 0;
      try {
        buyPrice = getOpeningPriceOnStartDate(getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(), endDate));
        sellPrice = getClosingPriceOnEndDate(getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(), endDate));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      annualizedReturns.add(calculateAnnualizedReturns(endDate, portfolioTrade, buyPrice, sellPrice));
    }
    Collections.sort(annualizedReturns, new Comparator<AnnualizedReturn>() {
      public int compare(AnnualizedReturn obj1, AnnualizedReturn obj2) {
        double firstAnnualizedReturn = obj1.getAnnualizedReturn();
        double secondAnnualizedReturn = obj2.getAnnualizedReturn();

        if (firstAnnualizedReturn < secondAnnualizedReturn) {
          return 1;
        } else if (firstAnnualizedReturn > secondAnnualizedReturn) {
          return -1;
        } else {
          return 0;
        }
      }
    });
    return annualizedReturns;
  }
  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF




  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws JsonProcessingException {
    List<Candle> candlesList = new ArrayList<>();
    String URL = buildUri(symbol, from, to);
    TiingoCandle[] tiingoCandles = getSerialisedObjects(URL);
    for (TiingoCandle tiingoCandle : tiingoCandles) {
      candlesList.add(tiingoCandle);
    }
    return candlesList;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
      String uriTemplate = String.format("https:api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s", symbol, startDate, endDate, PortfolioManagerApplication.getToken());
      return uriTemplate;
  }
}

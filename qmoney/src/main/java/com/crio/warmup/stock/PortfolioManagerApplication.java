
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  private static final String TIINGO_API_TOKEN = "684a52edff94ba7fabb8a57a51c98e9aa9eee72f";
  private static final RestTemplate restTemplate = new RestTemplate();
  
  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    List<String> symbolsList = new ArrayList<>();
    
    File tradesFile = resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = getObjectMapper();

    PortfolioTrade[] trades = objectMapper.readValue(tradesFile, PortfolioTrade[].class);
    for (PortfolioTrade trade : trades) {
      symbolsList.add(trade.getSymbol());
    }
    return symbolsList;
  }




  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.






  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/dineshraneen0912-ME_QMONEY_V2/qmoney/bin/main/trades.json";
     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@5542c4ed";
     String functionNameFromTestFileInStackTrace = "PortfolioManagerApplication.mainReadFile";
     String lineNumberFromTestFileInStackTrace = "29:1";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  public static TiingoCandle[] getSerialisedObjects(String URL) {
    RestTemplate restTemplate = new RestTemplate();
    TiingoCandle[] tiingoCandles = restTemplate.getForObject(URL, TiingoCandle[].class);
    return tiingoCandles;
  }

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    List<String> sortedStockList = new ArrayList<>();
    List<TotalReturnsDto> listOfSymbols = new ArrayList<>();
    LocalDate endDate = LocalDate.parse(args[1]);
    List<PortfolioTrade> trades = readTradesFromJson(args[0]);
    for (PortfolioTrade trade : trades) {
      String URL = prepareUrl(trade, endDate, TIINGO_API_TOKEN);
      TiingoCandle[] tiingoCandles = getSerialisedObjects(URL);
      TiingoCandle lastObject = tiingoCandles[tiingoCandles.length - 1];
      TotalReturnsDto totalReturnsDto = new TotalReturnsDto(trade.getSymbol(), lastObject.getClose());
      listOfSymbols.add(totalReturnsDto);
    }
    Collections.sort(listOfSymbols, new Comparator<TotalReturnsDto>() {
      public int compare(TotalReturnsDto obj1, TotalReturnsDto obj2) {
        double closingPriceForOne = obj1.getClosingPrice();
        double closingPriceForTwo = obj2.getClosingPrice();
        if (closingPriceForOne < closingPriceForTwo) {
          return -1;
        } else if (closingPriceForOne > closingPriceForTwo) {
          return 1;
        } else {
          return 0;
        }
      }
    });
    for (TotalReturnsDto totalReturnsDto : listOfSymbols) {
      sortedStockList.add(totalReturnsDto.getSymbol());
    }
    return sortedStockList;
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    List<PortfolioTrade> portfolioTrades = new ArrayList<>();
    
    File tradesFile = resolveFileFromResources(filename);
    ObjectMapper objectMapper = getObjectMapper();

    PortfolioTrade[] trades = objectMapper.readValue(tradesFile, PortfolioTrade[].class);
    for (PortfolioTrade trade : trades) {
      portfolioTrades.add(trade);
    }
    return portfolioTrades;
  }

  public static String getToken() {
    return TIINGO_API_TOKEN;
  }

  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    String URL = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s", trade.getSymbol(), trade.getPurchaseDate(), endDate, token);
    System.out.println(URL);
    return URL;
  }
  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    Candle startDay = candles.get(0);
    return startDay.getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    Candle endDay = candles.get(candles.size() - 1);
    return endDay.getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    List<Candle> candlesList = new ArrayList<>();
    String URL = prepareUrl(trade, endDate, token);
    TiingoCandle[] tiingoCandles = getSerialisedObjects(URL);
    for (TiingoCandle tiingoCandle : tiingoCandles) {
      candlesList.add(tiingoCandle);
    }
    return candlesList;
  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
        List<AnnualizedReturn> annualizedReturns = new ArrayList<>();
        List<PortfolioTrade> portfolioTrades = readTradesFromJson(args[0]);
        LocalDate endDate = LocalDate.parse(args[1]);
        for (PortfolioTrade trade : portfolioTrades) {
          List<Candle> tradeCandles = fetchCandles(trade, endDate, TIINGO_API_TOKEN);
          double buyPrice = getOpeningPriceOnStartDate(tradeCandles);
          double sellPrice = getClosingPriceOnEndDate(tradeCandles);
          AnnualizedReturn annualizedReturn = calculateAnnualizedReturns(endDate, trade, buyPrice, sellPrice);
          annualizedReturns.add(annualizedReturn);
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

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static long getPeriod(LocalDate startDate, LocalDate endDate) {
    long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    return daysBetween;
  }

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        LocalDate startDate = trade.getPurchaseDate();
        double totalNumDays = getPeriod(startDate, endDate);
        double totalReturns = ((sellPrice - buyPrice) * 1.0) / (buyPrice * 1.0);
        double annualizedReturns = Math.pow((1 + (totalReturns * 1.0)), (365 / (totalNumDays * 1.0))) - 1;
      return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturns);
  }



















  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.

  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       String file = args[0];
       PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager(restTemplate);
       LocalDate endDate = LocalDate.parse(args[1]);
      //  String contents = readFileAsString(file);
       ObjectMapper objectMapper = getObjectMapper();
       PortfolioTrade[] portfolioTrades = objectMapper.readValue(file, PortfolioTrade[].class);
       return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
  }


  // private static String readFileAsString(String file) {

  //   return null;
  // }




  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());




    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }
}


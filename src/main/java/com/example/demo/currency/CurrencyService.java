package com.example.demo.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    protected void fetchExchangeRates(){
        System.out.println("fetching from fixer.io");
        try {
            // configure api url and parameters
            String urlFixer = "http://data.fixer.io/api/latest?access_key={access_key}&symbols={symbols}";
            RestTemplate template = new RestTemplate();
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("access_key","989542825c6a0002c22d172cc28b3777");
            uriVariables.put("symbols","TRY, USD");
            // fetch results from api GET REQUEST
            Map<String, Object> result = template.getForObject(urlFixer, Map.class,uriVariables);
            // get exchange rates from response body as a map
            Map<String, Double> rates = (Map<String, Double>) result.get("rates");
            // api does not support source changing so calculate price of USDTRY
            double usd_d = rates.get("TRY") / rates.get("USD");

            // create new currency objects and save them to database
            Currency usd = new Currency("fixer.io", LocalDateTime.now(), "USD", usd_d);
            Currency eur = new Currency("fixer.io", LocalDateTime.now(), "EUR", rates.get("TRY"));

            currencyRepository.saveAll(List.of(usd,eur));


        }catch (Exception e){
            System.out.println("Error fetching from fixer.io: " + e.getMessage());
        }

        System.out.println("fetching from currencyLayer");
        try {
            // configure api url and parameters
            String urlCurrencyLayer = "http://api.currencylayer.com/live?access_key={access_key}&currencies={currencies}";
            RestTemplate template = new RestTemplate();
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("access_key","3a25fb863c0a351d7a07ceed96a97d35");
            uriVariables.put("currencies","TRY,EUR");
            // fetch results from api GET REQUEST
            Map<String, Object> result = template.getForObject(urlCurrencyLayer, Map.class,uriVariables);
            // get exchange rates from response body as a map
            Map<String, Double> rates = (Map<String, Double>) result.get("quotes");
            // api does not support source changing so calculate price of EURTRY
            double eur_d = rates.get("USDTRY") / rates.get("USDEUR");

            // create new currency objects and save them to database
            Currency eur = new Currency("currencyLayer", LocalDateTime.now(), "EUR", eur_d);
            Currency usd = new Currency("currencyLayer", LocalDateTime.now(), "USD", rates.get("USDTRY"));

            currencyRepository.saveAll(List.of(usd,eur));
        }catch (Exception e){
            System.out.println("Error fetching from currencyLayer: " + e.getMessage());
        }


    }

    public List<Currency> getCurrencyList(){
        return currencyRepository.findAll();
    }

    public List<Currency> getCurrencyList(String currencyType, String source, LocalDateTime date){
        return currencyRepository.findAllAggregate(currencyType,source,date);
    }
}

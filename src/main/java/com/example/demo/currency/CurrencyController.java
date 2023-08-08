package com.example.demo.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping(path="api/v1/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<Object> getCurrencyList(@RequestParam(required = false) String type,@RequestParam(required = false) String source,@RequestParam(required = false) String date){
        // check if provided currency is supported
        if(type != null && !type.equals("USD") && !type.equals("EUR")){
            return new ResponseEntity<>("Currency type not supported",HttpStatus.BAD_REQUEST);
        }

        //check if provided source is supported
        if(source != null && !source.equals("fixer.io") && !source.equals("currencyLayer")){
            return new ResponseEntity<>("source not supported",HttpStatus.BAD_REQUEST);
        }

        // check if provided date is valid
        LocalDateTime exchangeDate = null;
        if(date != null) {
            try {
                exchangeDate = LocalDateTime.parse(date).truncatedTo(ChronoUnit.SECONDS);
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>("invalid date: " + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        // check if fetching list is successful
        try {
            List<Currency> result = currencyService.getCurrencyList(type,source,exchangeDate);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Failed to fetch currency list: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

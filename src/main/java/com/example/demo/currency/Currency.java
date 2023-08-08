package com.example.demo.currency;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Table
public class Currency {
    @Id
    @SequenceGenerator(
            name = "currency_sequence",
            sequenceName = "currency_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "currency_sequence"
    )
    private Long id;
    private String source;
    private LocalDateTime exchangeDate;
    private String currencyType;
    private double exchangeRate;

    public Currency() {
    }

    public Currency(String source, LocalDateTime exchangeDate, String currencyType, double exchangeRate) {
        this.source = source;
        this.exchangeDate = exchangeDate.truncatedTo(ChronoUnit.SECONDS);
        this.currencyType = currencyType;
        this.exchangeRate = exchangeRate;
    }

    public String getSource() {
        return source;
    }

    public LocalDateTime getExchangeDate() {
        return exchangeDate;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public double getexchangeRate() {
        return exchangeRate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", exchangeDate=" + exchangeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                ", currencyType='" + currencyType + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}

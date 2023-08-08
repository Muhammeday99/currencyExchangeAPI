package com.example.demo.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query(value = "Select c From Currency c Where c.currencyType = ?1")
    List<Currency> findAllRatesByCurrencyType(String currencyType);


    @Query(value = "Select c From Currency c Where c.source = ?1")
    List<Currency> findAllRatesBySource(String source);

    @Query(value = "Select c From Currency c Where c.exchangeDate = ?1")
    List<Currency> findAllRatesByDate(LocalDateTime date);

    //filter database search according to parameters if parameter is null do not consider it as a filter criteria
    @Query("SELECT c FROM Currency c WHERE " +
            "(:source IS NULL OR c.source = :source) AND " +
            "(:currencyType IS NULL OR c.currencyType = :currencyType) AND" +
            "(cast(:exchangeDate as date) IS NULL OR c.exchangeDate = :exchangeDate)")
    List<Currency> findAllAggregate(@Param("currencyType") String currencyType,@Param("source") String source,@Param("exchangeDate") LocalDateTime date);

}

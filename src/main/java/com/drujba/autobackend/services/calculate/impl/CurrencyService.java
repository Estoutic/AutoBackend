//package com.drujba.autobackend.services.calculate.impl;
//
//import com.drujba.autobackend.models.enums.calculate.Currency;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//
//@Service
//public class CurrencyService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    // URL внешнего API (например, https://api.exchangeratesapi.io/latest?base=RUB)
//    @Value("${currency.api.url}")
//    private String currencyApiUrl;
//
//    public BigDecimal getExchangeRate(Currency currency) {
//        if (currency == Currency.RUB) {
//            return BigDecimal.ONE;
//        }
//        ResponseEntity<CurrencyApiResponse> response = restTemplate.getForEntity(currencyApiUrl, CurrencyApiResponse.class);
//        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            return response.getBody().getRateFor(currency);
//        } else {
//            throw new RuntimeException("Не удалось получить курс валют для " + currency);
//        }
//    }
//
//}

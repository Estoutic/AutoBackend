package com.drujba.autobackend.services.calculate.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Service to fetch currency exchange rates from the Central Bank of Russia API
 */
@Service
@Slf4j
public class ExchangeRateService {

    private static final String CBR_API_URL = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RestTemplate restTemplate;

    public ExchangeRateService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches current exchange rates from CBR API
     * Result is cached for one day to avoid excessive API calls
     *
     * @return Map of currency codes to exchange rates (to RUB)
     */
    @Cacheable(value = "exchangeRates", key = "#root.methodName")
    public Map<String, Double> getCurrentExchangeRates() {
        try {
            String date = LocalDate.now().format(DATE_FORMATTER);
            String url = CBR_API_URL + date;

            String response = restTemplate.getForObject(url, String.class);
            return parseExchangeRateXml(response);
        } catch (Exception e) {
            log.error("Error fetching exchange rates from CBR API", e);
            return getFallbackExchangeRates();
        }
    }

    /**
     * Gets the exchange rate for a specific currency to RUB
     *
     * @param currencyCode ISO currency code (USD, EUR, CNY, etc.)
     * @return Exchange rate to RUB or 1.0 if currency is RUB or not found
     */
    public double getExchangeRate(String currencyCode) {
        if ("RUB".equalsIgnoreCase(currencyCode)) {
            return 1.0;
        }

        Map<String, Double> rates = getCurrentExchangeRates();
        return rates.getOrDefault(currencyCode.toUpperCase(), 1.0);
    }

    /**
     * Parses XML response from CBR API
     */
    private Map<String, Double> parseExchangeRateXml(String xml) {
        Map<String, Double> rates = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);

            NodeList valutes = doc.getElementsByTagName("Valute");

            for (int i = 0; i < valutes.getLength(); i++) {
                Element valute = (Element) valutes.item(i);
                String charCode = valute.getElementsByTagName("CharCode").item(0).getTextContent();
                String valueStr = valute.getElementsByTagName("Value").item(0).getTextContent()
                        .replace(',', '.');
                String nominalStr = valute.getElementsByTagName("Nominal").item(0).getTextContent();

                double value = Double.parseDouble(valueStr);
                double nominal = Double.parseDouble(nominalStr);

                rates.put(charCode, value / nominal);
            }
        } catch (Exception e) {
            log.error("Error parsing exchange rate XML", e);
        }

        return rates;
    }

    /**
     * Fallback exchange rates in case API call fails
     */
    private Map<String, Double> getFallbackExchangeRates() {
        log.warn("Using fallback exchange rates");
        Map<String, Double> fallbackRates = new HashMap<>();
        fallbackRates.put("USD", 75.0);
        fallbackRates.put("EUR", 90.0);
        fallbackRates.put("CNY", 12.0);
        return fallbackRates;
    }
}
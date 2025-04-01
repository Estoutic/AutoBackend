package com.drujba.autobackend.services.conversion;

import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.services.calculate.impl.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class LocaleConversionService {

    private final ExchangeRateService exchangeRateService;

    private static final BigDecimal KM_TO_MILES_RATIO = new BigDecimal("0.621371");
    private static final int DECIMAL_SCALE = 2;

    /**
     * Converts mileage based on locale
     * - EU: kilometers to miles
     * - RU, ZH: keeps in kilometers
     *
     * @param mileage The mileage in kilometers
     * @param locale The target locale
     * @return Converted mileage based on locale
     */
    public BigDecimal convertMileage(BigDecimal mileage, Locale locale) {
        if (mileage == null) {
            return null;
        }

        if (locale == Locale.EU) {
            return mileage.multiply(KM_TO_MILES_RATIO).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
        }
        // For RU and ZH, keep original kilometers
        return mileage;
    }

    /**
     * Converts price from RUB to appropriate currency based on locale
     * - EU: RUB to USD
     * - ZH: RUB to CNY
     * - RU: keeps in RUB
     *
     * @param priceInRub The price in Russian Rubles
     * @param locale The target locale
     * @return Converted price based on locale
     */
    public BigDecimal convertPrice(BigDecimal priceInRub, Locale locale) {
        if (priceInRub == null) {
            return null;
        }

        switch (locale) {
            case EU:
                double usdRate = exchangeRateService.getExchangeRate("USD");
                return priceInRub.divide(BigDecimal.valueOf(usdRate), DECIMAL_SCALE, RoundingMode.HALF_UP);
            case ZH:
                double cnyRate = exchangeRateService.getExchangeRate("CNY");
                return priceInRub.divide(BigDecimal.valueOf(cnyRate), DECIMAL_SCALE, RoundingMode.HALF_UP);
            case RU:
            default:
                return priceInRub;
        }
    }

    /**
     * Returns currency code based on locale
     *
     * @param locale The target locale
     * @return Currency code for the locale
     */
    public String getCurrencyCode(Locale locale) {
        switch (locale) {
            case EU:
                return "USD";
            case ZH:
                return "CNY";
            case RU:
            default:
                return "RUB";
        }
    }

    /**
     * Determines if mileage should be displayed in miles based on locale
     *
     * @param locale The target locale
     * @return true if miles should be used, false for kilometers
     */
    public boolean isMiles(Locale locale) {
        return locale == Locale.EU;
    }
}
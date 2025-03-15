package com.drujba.autobackend.configs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "customs")
public class CustomsConfig {

    private int baseClearanceFee;
    private int baseUtilFee;
    private double etcUtilCoeffBase;
    private double ctpUtilCoeffBase;
    private Map<String, Integer> exciseRates;
    private Map<String, Map<String, Double>> recyclingFactors;
    private Map<String, Map<String, Map<String, Object>>> ageGroups;

    public int getBaseClearanceFee() {
        return baseClearanceFee;
    }

    public void setBaseClearanceFee(int baseClearanceFee) {
        this.baseClearanceFee = baseClearanceFee;
    }

    public int getBaseUtilFee() {
        return baseUtilFee;
    }

    public void setBaseUtilFee(int baseUtilFee) {
        this.baseUtilFee = baseUtilFee;
    }

    public double getEtcUtilCoeffBase() {
        return etcUtilCoeffBase;
    }

    public void setEtcUtilCoeffBase(double etcUtilCoeffBase) {
        this.etcUtilCoeffBase = etcUtilCoeffBase;
    }

    public double getCtpUtilCoeffBase() {
        return ctpUtilCoeffBase;
    }

    public void setCtpUtilCoeffBase(double ctpUtilCoeffBase) {
        this.ctpUtilCoeffBase = ctpUtilCoeffBase;
    }

    public Map<String, Integer> getExciseRates() {
        return exciseRates;
    }

    public void setExciseRates(Map<String, Integer> exciseRates) {
        this.exciseRates = exciseRates;
    }

    public Map<String, Map<String, Double>> getRecyclingFactors() {
        return recyclingFactors;
    }

    public void setRecyclingFactors(Map<String, Map<String, Double>> recyclingFactors) {
        this.recyclingFactors = recyclingFactors;
    }

    public Map<String, Map<String, Map<String, Object>>> getAgeGroups() {
        return ageGroups;
    }

    public void setAgeGroups(Map<String, Map<String, Map<String, Object>>> ageGroups) {
        this.ageGroups = ageGroups;
    }
}
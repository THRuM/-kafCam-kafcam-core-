package org.app.usecase.request;

public class CurrencyConverterRequestModel {

    private String sourceCurrency;
    private String targetCurrency;
    private Integer value;

    public CurrencyConverterRequestModel(String sourceCurrency, String targetCurrency, Integer value) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.value = value;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public Integer getValue() {
        return value;
    }
}

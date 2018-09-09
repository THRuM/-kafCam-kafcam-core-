package org.app.usecase.request;

import java.math.BigDecimal;

public class RecommendedCurrencyRequestModel {

    private String requestId;
    private String currencySymbol;
    private BigDecimal currencyQuantity;
    private Long timeStamp;
    private String email;

    public RecommendedCurrencyRequestModel(String requestId,
                                           String currencySymbol,
                                           BigDecimal currencyQuantity,
                                           Long timeStamp,
                                           String email) {

        this.requestId = requestId;
        this.currencySymbol = currencySymbol;
        this.currencyQuantity = currencyQuantity;
        this.timeStamp = timeStamp;
        this.email = email;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public BigDecimal getCurrencyQuantity() {
        return currencyQuantity;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getEmail() {
        return email;
    }
}

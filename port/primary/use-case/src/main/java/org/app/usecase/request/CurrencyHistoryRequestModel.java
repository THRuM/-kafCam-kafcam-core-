package org.app.usecase.request;

public class CurrencyHistoryRequestModel {

    private String requestId;
    private String currencySymbol;
    private Long timeStamp;
    private String email;


    public CurrencyHistoryRequestModel(String requestId, String currencySymbol, Long timeStamp, String email) {
        this.requestId = requestId;
        this.currencySymbol = currencySymbol;
        this.timeStamp = timeStamp;
        this.email = email;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getEmail() {
        return email;
    }
}

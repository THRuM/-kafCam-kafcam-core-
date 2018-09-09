package org.app.adapter.secondary.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collection;

@Document
public class RequestDataWrapper {

    @Id
    private String mongoId;
    private String requestId;
    private Collection<CurrencyData> currenciesData;
    private LocalDateTime generateData = LocalDateTime.now();

    public RequestDataWrapper() {
    }

    public RequestDataWrapper(String requestId, Collection<CurrencyData> currenciesData) {
        this.requestId = requestId;
        this.currenciesData = currenciesData;
    }


    public String getRequestId() {
        return requestId;
    }

    public Collection<CurrencyData> getCurrenciesData() {
        return currenciesData;
    }

    public LocalDateTime getGenerateData() {
        return generateData;
    }

    public String getMongoId() {
        return mongoId;
    }
}

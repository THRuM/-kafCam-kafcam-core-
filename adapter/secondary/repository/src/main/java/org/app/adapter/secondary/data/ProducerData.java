package org.app.adapter.secondary.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Producer")
public class ProducerData {
    @Id
    private String mongoId;

    private String producerId;
    private List<String> currenciesList;
    private Long interval;

    public ProducerData(String producerId, List<String> currenciesList, Long interval) {
        this.producerId = producerId;
        this.currenciesList = currenciesList;
        this.interval = interval;
    }

    public String getMongoId() {
        return mongoId;
    }

    public String getProducerId() {
        return producerId;
    }

    public List<String> getCurrenciesList() {
        return currenciesList;
    }

    public Long getInterval() {
        return interval;
    }
}

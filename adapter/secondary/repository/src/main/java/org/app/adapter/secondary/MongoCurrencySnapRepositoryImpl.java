package org.app.adapter.secondary;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.app.adapter.secondary.data.CurrencyData;
import org.app.domain.Currency;
import org.app.port.secondary.CurrencySnapRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Named
public class MongoCurrencySnapRepositoryImpl implements CurrencySnapRepository {


    @Value("${mongo.snapshot.time}")
    private String mongoTTL;

    @Value("${spring.data.mongodb.database}")
    private String mongoDbName;

    @Value("${mongo.snapshot.time}")
    private String mongoTLLTime;

    @Value("${mongo.ttl.index.name}")
    private String mongoTTLIndexName;

    @Value("${mongo.ttl.field.name}")
    private String mongoTTLFieldName;

    private CurrencyMemorySnapMongoStorage currencyMemorySnapMongoStorage;

    private MongoClient mongoClient;

    @PostConstruct
    public void init() {
        MongoDatabase database = mongoClient.getDatabase(mongoDbName);
        String collectionName = CurrencyData.class.getSimpleName();
        MongoCollection<Document> currencyDataCollection = database
                .getCollection(Character.toLowerCase(collectionName.charAt(0)) + collectionName.substring(1));

        currencyDataCollection.createIndex(Indexes.ascending(mongoTTLFieldName),
                new IndexOptions().expireAfter(Long.valueOf(mongoTLLTime), TimeUnit.MILLISECONDS));
    }


    @Inject
    public MongoCurrencySnapRepositoryImpl(CurrencyMemorySnapMongoStorage currencyMemorySnapMongoStorage, MongoClient mongoClient) {
        this.currencyMemorySnapMongoStorage = currencyMemorySnapMongoStorage;
        this.mongoClient = mongoClient;
    }

    @Override
    public void saveWithTTL(Collection<Currency> currenciesData) {

        //Adding the date to enable mongo tll deletion
        Date ttlDate = new Date();

        currencyMemorySnapMongoStorage
                .saveAll(currenciesData
                        .stream()
                        .map(currency -> new CurrencyData(currency, ttlDate))
                        .collect(Collectors.toSet()));
    }

    @Override
    public Collection<Currency> getCurrenciesForRequestId(String requestId) {

        return currencyMemorySnapMongoStorage
                .findAllByRequestId(requestId)
                .stream()
                .map(CurrencyData::toCurrency)
                .collect(Collectors.toList());
    }

    @Override
    public Long getOldestTime() {

        Optional<CurrencyData> oldestCurrencyData = currencyMemorySnapMongoStorage.findTop1ByOrderByTimeDesc();

        if (oldestCurrencyData.isPresent()) {
            return oldestCurrencyData.get().getTime();
        } else return Long.MIN_VALUE;
    }

    @Override
    public Collection<Currency> getCurrenciesForSymbolAndTimeBefore(String symbol, Long timeBefore) {

        return currencyMemorySnapMongoStorage
                .findAllBySymbolAndTimeLessThanEqual(symbol, timeBefore)
                .stream()
                .map(CurrencyData::toCurrency)
                .collect(Collectors.toList());
    }

}

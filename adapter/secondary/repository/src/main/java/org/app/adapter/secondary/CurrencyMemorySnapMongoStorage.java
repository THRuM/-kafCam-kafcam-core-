package org.app.adapter.secondary;

import org.app.adapter.secondary.data.CurrencyData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyMemorySnapMongoStorage extends MongoRepository<CurrencyData, String> {

    List<CurrencyData> findAllByRequestId(String requestId);

    Optional<CurrencyData> findTop1ByOrderByTimeDesc();

    List<CurrencyData> findAllBySymbolAndTimeLessThanEqual(String symbol, Long time);
}
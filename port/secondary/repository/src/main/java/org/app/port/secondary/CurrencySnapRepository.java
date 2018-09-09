package org.app.port.secondary;

import org.app.domain.Currency;

import java.util.Collection;

public interface CurrencySnapRepository {


    void saveWithTTL(Collection<Currency> currenciesData);

    Collection<Currency> getCurrenciesForRequestId(String requestId);

    Long getOldestTime();

    Collection<Currency> getCurrenciesForSymbolAndTimeBefore(String symbol, Long timeBefore);

}

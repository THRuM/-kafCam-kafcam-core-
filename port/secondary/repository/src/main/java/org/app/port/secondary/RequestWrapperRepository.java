package org.app.port.secondary;

import org.app.domain.Currency;

import java.util.Collection;

public interface RequestWrapperRepository {
    void save(String requestId, Collection<Currency> currenciesData);

    Collection<Currency> findCurrenciesDataById(String requestId);
}

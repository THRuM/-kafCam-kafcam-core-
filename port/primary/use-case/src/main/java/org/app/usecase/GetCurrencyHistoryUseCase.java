package org.app.usecase;

import org.app.domain.Currency;

import java.util.Collection;


public interface GetCurrencyHistoryUseCase {

    Collection<Currency> execute(String requestId);
}

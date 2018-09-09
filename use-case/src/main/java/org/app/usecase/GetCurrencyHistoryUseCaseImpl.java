package org.app.usecase;

import org.app.domain.Currency;
import org.app.port.secondary.CurrencySnapRepository;
import org.app.port.secondary.RequestWrapperRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

@Named
public class GetCurrencyHistoryUseCaseImpl implements GetCurrencyHistoryUseCase {

    private RequestWrapperRepository requestWrapperRepository;

    @Inject
    public GetCurrencyHistoryUseCaseImpl(RequestWrapperRepository requestWrapperRepository) {
        this.requestWrapperRepository = requestWrapperRepository;
    }

    @Override
    public Collection<Currency> execute(String requestId) {
        return requestWrapperRepository.findCurrenciesDataById(requestId);
    }
}

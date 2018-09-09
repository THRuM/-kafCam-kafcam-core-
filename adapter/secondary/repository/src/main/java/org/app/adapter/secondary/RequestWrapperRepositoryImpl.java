package org.app.adapter.secondary;

import org.app.adapter.secondary.data.CurrencyData;
import org.app.adapter.secondary.data.RequestDataWrapper;
import org.app.domain.Currency;
import org.app.port.secondary.RequestWrapperRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.stream.Collectors;

@Named
public class RequestWrapperRepositoryImpl implements RequestWrapperRepository {

    private RequestDataWrapperMongoStore requestDataWrapperMongoStore;

    @Inject
    public RequestWrapperRepositoryImpl(RequestDataWrapperMongoStore requestDataWrapperMongoStore) {
        this.requestDataWrapperMongoStore = requestDataWrapperMongoStore;
    }

    @Override
    public void save(String requestId, Collection<Currency> currenciesData) {

        requestDataWrapperMongoStore.save(new RequestDataWrapper(requestId, currenciesData
                .stream()
                .map(CurrencyData::new)
                .collect(Collectors.toList())));
    }

    @Override
    public Collection<Currency> findCurrenciesDataById(String requestId) {
        RequestDataWrapper requestDataForId = requestDataWrapperMongoStore.findByRequestId(requestId);
        return requestDataForId
                .getCurrenciesData()
                .stream()
                .map(CurrencyData::toCurrency)
                .collect(Collectors.toList());
    }
}

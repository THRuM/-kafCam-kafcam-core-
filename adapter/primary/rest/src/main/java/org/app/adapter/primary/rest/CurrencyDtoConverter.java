package org.app.adapter.primary.rest;

import org.app.adapter.primary.rest.model.CurrencyDto;
import org.app.domain.Currency;

class CurrencyDtoConverter {

    static CurrencyDto convert(Currency currency) {

        CurrencyDto delivery = new CurrencyDto();
        delivery.setAsk(currency.getAsk());
        delivery.setBid(currency.getBid());
        delivery.setPrice(currency.getPrice());
        delivery.setSymbol(currency.getSymbol());
        delivery.setTime(currency.getTimestamp());

        return delivery;
    }
}
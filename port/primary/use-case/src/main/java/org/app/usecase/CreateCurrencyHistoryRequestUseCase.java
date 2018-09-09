package org.app.usecase;

import org.app.usecase.request.CurrencyHistoryRequestModel;

public interface CreateCurrencyHistoryRequestUseCase {

    void execute(CurrencyHistoryRequestModel requestModel);
}

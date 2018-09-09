package org.app.usecase;

import org.app.usecase.request.CurrencyConverterRequestModel;

public interface StartConvertUseCase {

    void execute(CurrencyConverterRequestModel requestModel);
}

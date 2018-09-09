package org.app.usecase;

import org.app.usecase.request.CurrencyRecommendationRequestModel;

public interface AcceptCurrencyRecommendationUseCase {
    void execute(CurrencyRecommendationRequestModel currencyRecommendationRequestModel);
}

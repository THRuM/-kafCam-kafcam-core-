package org.app.usecase;

import org.app.usecase.request.RecommendedCurrencyRequestModel;

public interface CreateRecommendedCurrencyUseCase {

    void execute(RecommendedCurrencyRequestModel requestModel);
}

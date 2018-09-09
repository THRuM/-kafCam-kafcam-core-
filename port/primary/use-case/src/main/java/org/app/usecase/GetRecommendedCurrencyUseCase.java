package org.app.usecase;

import org.app.domain.Recommendation;

public interface GetRecommendedCurrencyUseCase {
    Recommendation execute(String requestId);
}

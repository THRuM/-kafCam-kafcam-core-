package org.app.usecase;

import org.app.domain.Recommendation;
import org.app.port.secondary.RecommendationRepository;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class GetRecommendedCurrencyUseCaseImpl implements GetRecommendedCurrencyUseCase {

    private RecommendationRepository recommendationRepository;

    @Inject
    public GetRecommendedCurrencyUseCaseImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Recommendation execute(String requestId) {
        return recommendationRepository.getRecommendationForRequestId(requestId);
    }
}

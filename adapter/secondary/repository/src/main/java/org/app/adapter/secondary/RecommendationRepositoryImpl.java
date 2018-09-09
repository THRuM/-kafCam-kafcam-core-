package org.app.adapter.secondary;

import org.app.domain.Recommendation;
import org.app.port.secondary.RecommendationRepository;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RecommendationRepositoryImpl implements RecommendationRepository {

    private RecommendationDataMongoStore recommendationDataMongoStore;

    @Inject
    public RecommendationRepositoryImpl(RecommendationDataMongoStore recommendationDataMongoStore) {
        this.recommendationDataMongoStore = recommendationDataMongoStore;
    }

    @Override
    public Recommendation getRecommendationForRequestId(String requestId) {
        return recommendationDataMongoStore.findByRequestId(requestId).toRecommendation();
    }
}

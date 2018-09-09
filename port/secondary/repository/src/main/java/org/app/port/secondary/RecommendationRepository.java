package org.app.port.secondary;

import org.app.domain.Recommendation;

public interface RecommendationRepository {
    Recommendation getRecommendationForRequestId(String requestId);
}

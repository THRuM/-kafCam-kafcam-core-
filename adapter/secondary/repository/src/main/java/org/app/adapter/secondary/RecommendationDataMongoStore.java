package org.app.adapter.secondary;


import org.app.adapter.secondary.data.RecommendationData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendationDataMongoStore extends MongoRepository<RecommendationData, String> {
    RecommendationData findByRequestId(String requestId);
}

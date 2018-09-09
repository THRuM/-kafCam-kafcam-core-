package org.app.adapter.primary.rest;

import org.app.adapter.primary.rest.model.RecommendationDTO;
import org.app.domain.Recommendation;

public class RecommendationDtoConverter {

    static RecommendationDTO convert(Recommendation recommendation) {
        RecommendationDTO recommendationDTO = new RecommendationDTO();

        recommendationDTO.setRequestId(recommendation.getRequestId());
        recommendationDTO.setCurrencyQuantity(recommendation.getCurrencyQuantity().longValue());
        recommendationDTO.setCurrencySymbol(recommendation.getCurrencySymbol());
        recommendationDTO.setOpinion(recommendation.getOpinion());
        recommendationDTO.setScore(recommendation.getScore());
        recommendationDTO.setTimeStamp(recommendation.getTimeStamp());

        return recommendationDTO;
    }
}

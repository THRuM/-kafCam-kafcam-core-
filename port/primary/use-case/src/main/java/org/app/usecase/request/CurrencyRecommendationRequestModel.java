package org.app.usecase.request;

public class CurrencyRecommendationRequestModel {
    private String requestId;
    private Integer score;
    private String opinion;

    public CurrencyRecommendationRequestModel(String requestId, Integer score, String opinion) {
        this.requestId = requestId;
        this.score = score;
        this.opinion = opinion;
    }

    public String getRequestId() {
        return requestId;
    }

    public Integer getScore() {
        return score;
    }

    public String getOpinion() {
        return opinion;
    }
}
package org.app.adapter.secondary.data;

import org.app.domain.Recommendation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "recommendationData")
public class RecommendationData {

    @Id
    private String mongoId;
    private String requestId;
    private String currencySymbol;
    private Long timeStamp;
    private BigDecimal currencyQuantity;
    private Integer score;
    private String opinion;

    public Recommendation toRecommendation() {
        return new Recommendation(requestId, currencySymbol, timeStamp, currencyQuantity, score, opinion);
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getCurrencyQuantity() {
        return currencyQuantity;
    }

    public void setCurrencyQuantity(BigDecimal currencyQuantity) {
        this.currencyQuantity = currencyQuantity;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}

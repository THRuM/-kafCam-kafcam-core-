package org.app.usecase;

import org.app.common.util.EventParams;
import org.app.domain.Event;
import org.app.port.secondary.ApplicationEventManager;
import org.app.usecase.request.CurrencyRecommendationRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
public class AcceptCurrencyRecommendationUseCaseImpl implements AcceptCurrencyRecommendationUseCase {

    public static final Logger LOG = LoggerFactory.getLogger(AcceptCurrencyRecommendationUseCaseImpl.class);

    private ApplicationEventManager applicationEventManager;

    @Value("${business.topic}")
    private String businessTopic;

    @Inject
    public AcceptCurrencyRecommendationUseCaseImpl(ApplicationEventManager applicationEventManager) {
        this.applicationEventManager = applicationEventManager;
    }

    @Override
    public void execute(CurrencyRecommendationRequestModel requestModel) {
        LOG.info("Accepting currency recommendation for requestId {}", requestModel.getRequestId());

        Map<String, Object> eventParams = new HashMap<>();

        eventParams.put(EventParams.REQUEST_ID, requestModel.getRequestId());
        eventParams.put(EventParams.CURRENCY_SCORE, requestModel.getScore());
        eventParams.put(EventParams.CURRENCY_OPINION, requestModel.getOpinion());
        Event event = new Event(requestModel.getRequestId(), "org.app.domain.event.RecommendationResponseEvent",
                eventParams);

        applicationEventManager.onEvent(event, businessTopic);
        LOG.info("event creator for RecommendationResponseEvent stopped");
    }
}

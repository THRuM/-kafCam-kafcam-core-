package org.app.usecase;

import org.app.common.util.EventParams;
import org.app.domain.Event;
import org.app.port.secondary.ApplicationEventManager;
import org.app.usecase.request.RecommendedCurrencyRequestModel;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Named
public class CreateRecommendedCurrencyUseCaseImpl implements CreateRecommendedCurrencyUseCase {

    public static final Logger LOG = LoggerFactory.getLogger(CreateRecommendedCurrencyUseCaseImpl.class);

    private ApplicationEventManager applicationEventManager;

    @Value("${business.topic}")
    private String businessTopic;

    @Inject
    public CreateRecommendedCurrencyUseCaseImpl(ApplicationEventManager applicationEventManager) {
        this.applicationEventManager = applicationEventManager;
    }

    @Override
    public void execute(RecommendedCurrencyRequestModel requestModel) {

        LOG.info("event creator for CreateRecommendedCurrencyUseCase started");
        Map<String, Object> eventParams = new HashMap<>();

        eventParams.put(EventParams.CURRENCY_SYMBOL, requestModel.getCurrencySymbol());
        eventParams.put(EventParams.CURRENCY_QUANTITY, requestModel.getCurrencyQuantity());
        eventParams.put(EventParams.TIMESTAMP, requestModel.getTimeStamp());
        Event event = new Event(requestModel.getRequestId(), "org.app.domain.event.RecommendedRequestEvent",
                eventParams);

        applicationEventManager.onEvent(event, businessTopic);
        LOG.info("event creator for CreateRecommendedCurrencyUseCase stopped");

    }
}

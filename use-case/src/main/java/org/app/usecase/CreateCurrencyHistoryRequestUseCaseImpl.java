package org.app.usecase;

import org.app.common.util.EventParams;
import org.app.domain.Event;
import org.app.port.secondary.ApplicationEventManager;
import org.app.usecase.request.CurrencyHistoryRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
public class CreateCurrencyHistoryRequestUseCaseImpl implements CreateCurrencyHistoryRequestUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(CreateCurrencyHistoryRequestUseCaseImpl.class);

    private ApplicationEventManager applicationEventManager;

    @Value("${application.topic}")
    private String applicationTopic;

    @Inject
    public CreateCurrencyHistoryRequestUseCaseImpl(ApplicationEventManager applicationEventManager) {
        this.applicationEventManager = applicationEventManager;
    }

    @Override
    public void execute(CurrencyHistoryRequestModel requestModel) {

        LOG.info("event creator for CreateCurrencyHistoryRequestUseCase started");
        Map<String, Object> eventParams = new HashMap<>();

        eventParams.put(EventParams.CURRENCY_SYMBOL, requestModel.getCurrencySymbol());
        eventParams.put(EventParams.CURRENCY_TIME, requestModel.getTimeStamp());
        Event event = new Event(requestModel.getRequestId(), "org.app.domain.event.HistoryRequestEvent",
                eventParams);

        applicationEventManager.onEvent(event, applicationTopic);
        LOG.info("event creator for CreateCurrencyHistoryRequestUseCase stopped");

    }
}

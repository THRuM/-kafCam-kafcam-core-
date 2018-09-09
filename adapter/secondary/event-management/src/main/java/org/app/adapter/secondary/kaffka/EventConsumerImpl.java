package org.app.adapter.secondary.kaffka;

import org.app.domain.Currency;
import org.app.domain.Producer;
import org.app.domain.event.CannotCompleteHistoryEvent;
import org.app.domain.event.CompleteHistoryEvent;
import org.app.domain.event.HistoryRequestEvent;
import org.app.domain.event.ProducerRegisteredEvent;
import org.app.port.secondary.CurrencySnapRepository;
import org.app.port.secondary.ProducerRepository;
import org.app.port.secondary.RequestWrapperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Named
@KafkaListener(topics = {"${application.topic}"})
public class EventConsumerImpl implements EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerImpl.class);
    private CurrencySnapRepository currencySnapRepository;
    private RequestWrapperRepository requestWrapperRepository;
    private ProducerRepository producerRepository;
    private EventSender eventSender;

    @Inject
    public EventConsumerImpl(CurrencySnapRepository currencySnapRepository,
                             RequestWrapperRepository requestWrapperRepository,
                             EventSender eventSender,
                             ProducerRepository producerRepository) {
        this.currencySnapRepository = currencySnapRepository;
        this.requestWrapperRepository = requestWrapperRepository;
        this.eventSender = eventSender;
        this.producerRepository = producerRepository;
    }

    @Value("${notification.topic}")
    private String notificationTopic;

    @Value("${worker.topic}")
    private String workerTopic;


    @Override
    @KafkaHandler
    public void consume(HistoryRequestEvent historyRequestEvent) {
        log.info("consume event with request id:  {}", historyRequestEvent.getRequestId());

        if (historyRequestEvent.getTimeStamp() <= currencySnapRepository.getOldestTime()) {
            log.info("Data in mongo snap - getting the data for event with request id: {}", historyRequestEvent.getRequestId());

            Collection<Currency> currencies = currencySnapRepository.getCurrenciesForSymbolAndTimeBefore(
                    historyRequestEvent.getCurrencySymbol(), historyRequestEvent.getTimeStamp());

            if (currencies.isEmpty()) {
                eventSender.send(new CannotCompleteHistoryEvent("No data available in snap",
                        historyRequestEvent.getRequestId(),
                        historyRequestEvent), notificationTopic);
            } else {
                requestWrapperRepository.save(historyRequestEvent.getRequestId(), currencies);

                eventSender.send(new CompleteHistoryEvent(historyRequestEvent.getRequestId()), notificationTopic);
            }

        } else {
            log.info("Data not in mongo snap");
            eventSender.send(historyRequestEvent.toDataNotInCacheEvent(), workerTopic);
        }
    }

    @Override
    @KafkaHandler
    public void consume(ProducerRegisteredEvent producerRegisteredEvent) {
        log.info("consume producer registered event for producerId {}", producerRegisteredEvent.getProducerId());

        List<String> currenciesList = Arrays.asList(producerRegisteredEvent.getCurrencies().split(","));

        Producer producer = new Producer(producerRegisteredEvent.getProducerId(),
                currenciesList,
                producerRegisteredEvent.getInterval());

        producerRepository.save(producer);
    }
}

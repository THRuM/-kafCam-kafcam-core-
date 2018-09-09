package org.app.adapter.secondary.kaffka;

import org.app.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EventSenderImpl implements EventSender {

    private static final Logger log = LoggerFactory.getLogger(EventSenderImpl.class);

    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    @Inject
    public EventSenderImpl(KafkaTemplate<String, DomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(DomainEvent kafkaEvent, String topic) {
        log.info("event [{}] published to topic {}", kafkaEvent.getClass(), topic);
        kafkaTemplate.send(topic, kafkaEvent.getRequestId(), kafkaEvent);
    }
}
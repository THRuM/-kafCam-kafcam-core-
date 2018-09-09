package org.app.adapter.secondary.kaffka;

import org.app.domain.event.DomainEvent;

interface EventSender {

    void send(DomainEvent kafkaEvent, String topic);
}

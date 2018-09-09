package org.app.adapter.secondary.kaffka;

import org.app.domain.event.HistoryRequestEvent;
import org.app.domain.event.ProducerRegisteredEvent;

public interface EventConsumer {

    void consume(HistoryRequestEvent historyRequestEvent);

    void consume(ProducerRegisteredEvent producerRegisteredEvent);

}

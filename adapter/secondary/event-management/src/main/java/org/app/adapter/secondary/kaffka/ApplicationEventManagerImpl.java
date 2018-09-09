package org.app.adapter.secondary.kaffka;

import org.app.common.exception.BadClassEventException;
import org.app.domain.Event;
import org.app.domain.event.DomainEvent;
import org.app.port.secondary.ApplicationEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Named
public class ApplicationEventManagerImpl implements ApplicationEventManager {

    private final static Logger log = LoggerFactory.getLogger(ApplicationEventManagerImpl.class);

    private EventSender eventSender;

    @Inject
    public ApplicationEventManagerImpl(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    @Override
    public void onEvent(Event event, String topic) {
        String className = event.getEventClassName();

        log.info("create specific event for class name [{}]", className);

        try {
            Class<?> eventClass = Class.forName(className);
            Constructor<?> eventClassConstructor = eventClass.getConstructor(String.class, Map.class);
            DomainEvent kafkaEvent = (DomainEvent) eventClassConstructor.newInstance(event.getRequestId(), event.getParams());
            log.info("event [{}] has been created", className);
            eventSender.send(kafkaEvent, topic);
            log.info("event [{}] has been sended to kafka", className);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            log.error("Request created error: ", e.getMessage());
            throw new BadClassEventException(e.getMessage());
        }

    }
}

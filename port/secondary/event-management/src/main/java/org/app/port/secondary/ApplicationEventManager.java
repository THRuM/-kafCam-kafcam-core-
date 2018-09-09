package org.app.port.secondary;

import org.app.domain.Event;

public interface ApplicationEventManager {

    void onEvent(Event event, String topic);

}

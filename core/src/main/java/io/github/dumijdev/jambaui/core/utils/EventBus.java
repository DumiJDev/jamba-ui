package io.github.dumijdev.jambaui.core.utils;

import java.util.LinkedList;
import java.util.List;

public class EventBus {
    private final List<EventListener> listeners = new LinkedList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void fireEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.handleEvent(event);
        }
    }
}

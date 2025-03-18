package io.github.dumijdev.jambaui.core.events;

import java.util.LinkedList;
import java.util.List;

public class EventBus {
    private final List<EventListener<? extends Event>> listeners = new LinkedList<>();

    public <T extends Event> void registerListener(EventListener<T> listener) {
        listeners.add(listener);
    }

    public <T extends Event> void fireEvent(T event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

package io.github.dumijdev.jambaui.core.events;

public interface EventListener<T extends Event> {
    void onEvent(T event);
}

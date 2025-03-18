package io.github.dumijdev.jambaui.core.events;

public class ClickEvent implements Event {
    private final Object source;

    public ClickEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}

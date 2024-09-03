package io.github.dumijdev.jambaui.core.events;

public class ChangeValueEvent implements Event {
    private final String value;

    public ChangeValueEvent(String newText) {
        this.value = newText;
    }

    public String getValue() {
        return value;
    }
}

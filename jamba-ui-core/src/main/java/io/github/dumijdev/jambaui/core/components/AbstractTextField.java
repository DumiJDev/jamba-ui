package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.core.events.ChangeValueEvent;
import io.github.dumijdev.jambaui.core.events.EventListener;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTextField<I> implements Component<I> {
    private String text;
    private EventListener<ChangeValueEvent> changeValueEventEventListener;
    protected Style style;
    private final Map<String, Object> properties = new HashMap<>();

    public AbstractTextField() {
        this("");
    }

    public AbstractTextField(String initialValue) {
        this(initialValue, null);
    }

    public AbstractTextField(String initialValue, EventListener<ChangeValueEvent> changeValueEventListener) {
        text = initialValue;
        this.changeValueEventEventListener = changeValueEventListener;
    }

    public String getValue() {
        return text;
    }

    public void setValue(String value) {
        text = value;

        if (changeValueEventEventListener != null) {
            changeValueEventEventListener.onEvent(new ChangeValueEvent(text));
        }
    }

    @Override
    public void add(Component<?>... component) {
        throw new UnsupportedOperationException("TextField cannot contain other components.");
    }

    @Override
    public void remove(Component<?> component) {
        throw new UnsupportedOperationException("TextField cannot contain other components.");
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void addClickListener(EventListener<ChangeValueEvent> listener) {
        this.changeValueEventEventListener = listener;
    }

    @Override
    public Style getStyle() {
        return style;
    }
}

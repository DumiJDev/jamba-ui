package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.events.ClickEvent;
import io.github.dumijdev.jambaui.core.events.EventListener;

public abstract class AbstractButton<I> implements Component<I> {
    private String text;
    private EventListener<ClickEvent> listener;

    public AbstractButton() {
        this("Button");
    }

    public AbstractButton(String text) {
        this.text = text;
    }

    public AbstractButton(String text, EventListener<ClickEvent> listener) {
        this.text = text;
        this.listener = listener;
    }

    public void addClickListener(EventListener<ClickEvent> listener) {
        this.listener = listener;
    }

    public void click() {
        if (listener != null) {
            listener.onEvent(new ClickEvent());
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void add(Component<?>... component) {
        throw new UnsupportedOperationException("Button cannot contain other components.");
    }

    @Override
    public void remove(Component<?> component) {
        throw new UnsupportedOperationException("Button cannot contain other components.");
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

}

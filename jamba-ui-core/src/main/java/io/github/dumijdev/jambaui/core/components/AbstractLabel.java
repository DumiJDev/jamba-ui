package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.core.events.ClickEvent;
import io.github.dumijdev.jambaui.core.events.EventListener;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLabel<I> implements Component<I> {
    private String text;
    private EventListener<ClickEvent> clickListener;
    protected Style style;
    private final Map<String, Object> properties = new HashMap<>();

    public AbstractLabel() {
        this("Label", null);
    }

    public AbstractLabel(String text) {
        this(text, null);
    }

    public AbstractLabel(String text, EventListener<ClickEvent> clickListener) {
        this.text = text;
        this.clickListener = clickListener;
    }

    public void addClickListener(EventListener<ClickEvent> listener) {
        this.clickListener = listener;
    }

    public void click() {
        if (clickListener != null) {
            this.clickListener.onEvent(new ClickEvent(this));
        }
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Style getStyle() {
        return style;
    }
}

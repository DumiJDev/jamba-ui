package io.github.dumijdev.jambaui.core.layouts;

import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.core.events.EventBus;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHorizontalLayout<I> implements Layout<I> {

    private final EventBus eventBus = new EventBus();
    protected Style style;
    private final Map<String, Object> properties;

    public AbstractHorizontalLayout() {
        properties = new HashMap<>();
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public Style getStyle() {
        return style;
    }
}

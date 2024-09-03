package io.github.dumijdev.jambaui.core.layouts;

import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.StyleObject;
import io.github.dumijdev.jambaui.core.events.EventBus;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVerticalLayout<I> implements Layout<I> {
    private final EventBus eventBus = new EventBus();
    private String title;
    private final Style style;
    private final Map<String, Object> properties;

    public AbstractVerticalLayout() {
        style = new StyleObject();
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

}

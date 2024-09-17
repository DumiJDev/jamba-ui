package io.github.dumijdev.jambaui.core.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.events.EventBus;
import io.github.dumijdev.jambaui.core.style.Style;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAppLayout<I> implements Layout<I> {

    private final EventBus eventBus = new EventBus();
    private String text;
    protected Style style;
    private final Map<String, Object> properties;

    public AbstractAppLayout() {
        properties = new HashMap<>();
    }

    public abstract void addToSideBar(Component<?>... component);

    public abstract void addToHeader(Component<?> component);

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

}

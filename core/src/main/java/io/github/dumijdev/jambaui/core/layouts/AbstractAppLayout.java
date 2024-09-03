package io.github.dumijdev.jambaui.core.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.StyleObject;
import io.github.dumijdev.jambaui.core.components.AbstractButton;
import io.github.dumijdev.jambaui.core.events.EventBus;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAppLayout<I> implements Layout<I> {

    private final EventBus eventBus = new EventBus();
    private String text;
    private final Style style;
    private final Map<String, Object> properties;

    public AbstractAppLayout() {
        style = new StyleObject();
        properties = new HashMap<>();
    }

    public abstract void addToSideBar(AbstractButton<?> button);

    public abstract void addToContentArea(Component<?> component);

    public abstract void addToHeader(AbstractButton<?> button);

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

}

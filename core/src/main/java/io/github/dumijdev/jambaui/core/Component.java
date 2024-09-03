package io.github.dumijdev.jambaui.core;

import java.util.HashMap;
import java.util.Map;

public interface Component<INTERNAL_COMPONENT> {
    Style style = new StyleObject();
    Map<String, Object> properties = new HashMap<>();

    void add(Component<?>...component);
    void remove(Component<?> component);

    default Style getStyle() {
        return style;
    }

    void setProperty(String name, Object value);
    Object getProperty(String name);
    void update();
    INTERNAL_COMPONENT getInternal();
}

package io.github.dumijdev.jambaui.core;

import io.github.dumijdev.jambaui.core.style.Style;

import java.io.Serializable;

public interface Component<INTERNAL_COMPONENT> extends Serializable {


    void add(Component<?>...component);
    void remove(Component<?> component);

    Style getStyle();

    void setProperty(String name, Object value);
    Object getProperty(String name);
    void update();
    INTERNAL_COMPONENT getInternal();
}

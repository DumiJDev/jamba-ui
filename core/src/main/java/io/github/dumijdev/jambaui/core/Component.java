package io.github.dumijdev.jambaui.core;

public interface Component<INTERNAL_COMPONENT> {


    void add(Component<?>...component);
    void remove(Component<?> component);

    Style getStyle();

    void setProperty(String name, Object value);
    Object getProperty(String name);
    void update();
    INTERNAL_COMPONENT getInternal();
}

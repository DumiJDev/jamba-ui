package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.utils.EventBus;
import io.github.dumijdev.jambaui.core.utils.EventListener;

public abstract class AbstractButton<I> implements Component<I> {
    private final EventBus eventBus = new EventBus();
    private String text;

    public AbstractButton(String text) {
        this.text = text;
    }

    public void addClickListener(EventListener listener) {
        eventBus.registerListener(listener);
    }

    public abstract void click();

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void add(Component<?> component) {
        throw new UnsupportedOperationException("Button cannot contain other components.");
    }

    @Override
    public void remove(Component<?> component) {
        throw new UnsupportedOperationException("Button cannot contain other components.");
    }

    @Override
    public Style getStyle() {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) {
        if ("text".equals(name)) {

        } else if ("style".equals(name)) {

        }
    }

    @Override
    public Object getProperty(String name) {
        if ("text".equals(name)) {
            return null;
        } else if ("style".equals(name)) {
            return null;
        }
        return null;
    }

}

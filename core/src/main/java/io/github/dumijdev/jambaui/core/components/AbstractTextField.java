package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;

public abstract class AbstractTextField<I> implements Component<I> {

    public AbstractTextField(String initialValue) {

    }

    public abstract String getValue();

    public abstract void setValue(String value);

    @Override
    public void add(Component<?> component) {
        throw new UnsupportedOperationException("TextField cannot contain other components.");
    }

    @Override
    public void remove(Component<?> component) {
        throw new UnsupportedOperationException("TextField cannot contain other components.");
    }

    @Override
    public void setProperty(String name, Object value) {
        if ("value".equals(name)) {

        } else if ("style".equals(name)) {

        }
    }

    @Override
    public Object getProperty(String name) {
        if ("value".equals(name)) {
            return null;
        } else if ("style".equals(name)) {
            return null;
        }
        return null;
    }
}

package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;

public abstract class AbstractLabel<I> implements Component<I> {

    public AbstractLabel(String text) {

    }


    @Override
    public void setProperty(String name, Object value) {
        if ("style".equals(name)) {

        } else if ("text".equals(name)) {

        }
    }

    @Override
    public Object getProperty(String name) {
        if ("style".equals(name)) {
            return null;
        } else if ("text".equals(name)) {
            return null;
        }
        return null;
    }
}

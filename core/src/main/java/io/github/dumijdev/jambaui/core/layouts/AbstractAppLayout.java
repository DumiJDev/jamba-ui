package io.github.dumijdev.jambaui.core.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.components.AbstractButton;

public abstract class AbstractAppLayout<I> implements Layout<I> {


    public AbstractAppLayout() {

    }

    public abstract void addToSideBar(AbstractButton<?> button);

    public abstract void addToContentArea(Component<?> component);

    public abstract void addToHeader(AbstractButton<?> button);

    @Override
    public void setProperty(String name, Object value) {
        if ("style".equals(name)) {

        }
    }

    @Override
    public Object getProperty(String name) {
        if ("style".equals(name)) {
            return null;
        }
        return null;
    }
}

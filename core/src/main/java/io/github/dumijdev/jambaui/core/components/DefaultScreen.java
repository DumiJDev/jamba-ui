package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.layouts.Layout;

public class DefaultScreen<L, C> implements Screen<L, C> {
    private final Layout<L> layout;
    private final Component<C> component;

    public DefaultScreen(Layout<L> layout, Component<C> component) {
        this.layout = layout;
        this.component = component;
    }

    @Override
    public Layout<L> layout() {
        return layout;
    }

    @Override
    public Component<C> component() {
        return component;
    }
}

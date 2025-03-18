package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.layouts.Layout;

public record DefaultScreen<L, C>(Layout<L> layout, Component<C> component) implements Screen<L, C> {
}

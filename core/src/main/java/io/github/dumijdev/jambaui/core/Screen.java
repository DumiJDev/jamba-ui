package io.github.dumijdev.jambaui.core;

import io.github.dumijdev.jambaui.core.layouts.Layout;

public interface Screen<L, C> {
    Layout<L> layout();
    Component<C> component();
}

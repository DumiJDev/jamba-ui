package io.github.dumijdev.jambaui.core.layouts;

import io.github.dumijdev.jambaui.core.Component;

public interface Layout<I> extends Component<I> {
    Layout<I> copy();
}

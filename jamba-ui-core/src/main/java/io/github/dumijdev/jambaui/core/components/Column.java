package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;

import java.util.function.Function;

public interface Column<T> {
    String getName();

    double getWidth();

    String getValue();

    Column<T> name(String name);

    Column<T> width(int width);

    Column<T> value(Function<T, Object> fun);

    Column<T> action(Function<T, ? extends Component<?>> fun);

}

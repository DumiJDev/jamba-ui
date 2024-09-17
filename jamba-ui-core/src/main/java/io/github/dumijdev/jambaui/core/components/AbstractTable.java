package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.style.Style;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractTable<T, K, I> implements Component<I> {
    private final List<Column> columns;
    private List<Row<T>> rows;
    protected Style style;
    private final Map<String, Object> properties = new HashMap<>();

    public AbstractTable() {
        columns = new LinkedList<>();
        rows = new LinkedList<>();
    }

    public Column addColumn() {
        var column = new Column();
        columns.add(column);
        return column;
    }

    public void setItems(T... items) {
        var items1 = List.of(items);

        rows = items1.stream().map(Row::new).toList();

        update();
    }

    @Override
    public void add(Component<?>... component) {
        throw new UnsupportedOperationException("Table cannot contain other components.");
    }

    @Override
    public void remove(Component<?> component) {
        throw new UnsupportedOperationException("Table cannot contain other components.");
    }


    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public Style getStyle() {
        return style;
    }
}

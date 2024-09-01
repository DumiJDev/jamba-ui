package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTable<T, K, I> implements Component<I> {
    private final List<Column> columns;
    private List<Row<T>> rows;

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
    public void add(Component<?> component) {
        throw new UnsupportedOperationException("Table cannot contain other components.");
    }

    @Override
    public void remove(Component<?> component) {
        throw new UnsupportedOperationException("Table cannot contain other components.");
    }


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

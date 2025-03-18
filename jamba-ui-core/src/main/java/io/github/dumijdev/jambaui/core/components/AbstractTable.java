package io.github.dumijdev.jambaui.core.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.style.Style;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractTable<T, I> implements Component<I> {
    private final Map<String, Object> properties = new HashMap<>();

    public AbstractTable() {
    }

    public abstract Column<T> addColumn();

    public abstract void setItems(T... items);

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

}

package io.github.dumijdev.jambaui.core.components;

public class Row<T> {
    private Object id;
    private final T data;

    public Row(T t) {
        this.data = t;
    }

    public Object getId() {
        return id;
    }

    public T getData() {
        return data;
    }
}

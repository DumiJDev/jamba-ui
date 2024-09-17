package io.github.dumijdev.jambaui.core.components;

import java.util.function.Function;

public class Column {
    private String name;
    private int width;
    private String value;

    public Column() {
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public String getValue() {
        return value;
    }

    public Column name(String name) {
        this.name = name;
        return this;
    }

    public Column width(int width) {
        this.width = width;
        return this;
    }

    public Column value(String value) {
        this.value = value;
        return this;
    }

}

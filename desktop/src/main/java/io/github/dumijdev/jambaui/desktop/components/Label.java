package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.components.AbstractLabel;

public class Label extends AbstractLabel<javafx.scene.control.Label> {
    private final javafx.scene.control.Label label;

    public Label(String text) {
        super(text);
        this.label = new javafx.scene.control.Label(text);
    }

    @Override
    public void add(Component<?> component) {

    }

    @Override
    public void remove(Component<?> component) {

    }

    @Override
    public Style getStyle() {
        return null;
    }

    @Override
    public void update() {
        label.layout();
    }

    @Override
    public javafx.scene.control.Label getInternal() {
        return this.label;
    }
}

package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.components.AbstractLabel;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import javafx.event.ActionEvent;

public class Label extends AbstractLabel<javafx.scene.control.Label> {
    private final javafx.scene.control.Label label;

    public Label(String text) {
        super(text);
        this.label = new javafx.scene.control.Label(text);
        this.style = new ComponentStyle(label);
    }

    @Override
    public void click() {
        label.fireEvent(new ActionEvent());
    }

    @Override
    public void add(Component<?>... component) {

    }

    @Override
    public void remove(Component<?> component) {

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

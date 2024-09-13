package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.components.AbstractTextField;

public class TextField extends AbstractTextField<javafx.scene.control.TextField> {
    private final javafx.scene.control.TextField textField;

    public TextField(String initialValue) {
        super(initialValue);
        textField = new javafx.scene.control.TextField(initialValue);
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(String value) {
        textField.setText(value);
    }

    @Override
    public void update() {
        textField.layout();
    }

    @Override
    public javafx.scene.control.TextField getInternal() {
        return textField;
    }
}

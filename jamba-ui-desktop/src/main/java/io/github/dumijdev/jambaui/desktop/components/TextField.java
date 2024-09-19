package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.components.AbstractTextField;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import io.github.palexdev.materialfx.controls.MFXTextField;

public class TextField extends AbstractTextField<MFXTextField> {
    private final MFXTextField textField;

    public TextField(String initialValue) {
        super(initialValue);
        textField = new MFXTextField(initialValue);
        this.style = new ComponentStyle(textField);
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
    public MFXTextField getInternal() {
        return textField;
    }
}

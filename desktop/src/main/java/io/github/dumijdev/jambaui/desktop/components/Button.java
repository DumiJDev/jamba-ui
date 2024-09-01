package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.components.AbstractButton;
import io.github.dumijdev.jambaui.core.utils.Event;

public class Button extends AbstractButton<javafx.scene.control.Button> {
    private final javafx.scene.control.Button button;

    public Button() {
        super("Button");
        button = new javafx.scene.control.Button(getText());
    }

    public Button(String text) {
        this();
    }

    public Button(String text, Event event) {
        this(text);
    }

    @Override
    public void click() {

    }

    @Override
    public void update() {
        button.layout();
    }

    @Override
    public javafx.scene.control.Button getInternal() {
        return button;
    }
}

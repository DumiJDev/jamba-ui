package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.components.AbstractButton;
import io.github.dumijdev.jambaui.core.events.ClickEvent;
import io.github.dumijdev.jambaui.core.events.EventListener;

public class Button extends AbstractButton<javafx.scene.control.Button> {
    private final javafx.scene.control.Button button;

    public Button() {
        this("Button", null);
    }

    public Button(String text) {
        this(text, null);
    }

    public Button(String text, EventListener<ClickEvent> listener) {
        super(text, listener);
        button = new javafx.scene.control.Button(getText());

        button.setOnAction(actionEvent -> {
            listener.onEvent(new ClickEvent());
        });
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

package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.components.AbstractButton;
import io.github.dumijdev.jambaui.core.events.ClickEvent;
import io.github.dumijdev.jambaui.core.events.EventListener;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import io.github.palexdev.materialfx.controls.MFXButton;

public class Button extends AbstractButton<MFXButton> {
    private final MFXButton button;

    public Button() {
        this("Button", null);
    }

    public Button(String text) {
        this(text, null);
    }

    public Button(String text, EventListener<ClickEvent> listener) {
        super(text, listener);
        button = new MFXButton(getText());

        this.style = new ComponentStyle(button);

        if (listener != null) {
            button.setOnAction(actionEvent -> {
                listener.onEvent(new ClickEvent(this));
            });
        }
    }

    @Override
    public void update() {
        button.layout();
    }

    @Override
    public MFXButton getInternal() {
        return button;
    }

    @Override
    public void addClassName(String className) {
        button.getStyleClass().add(className);
    }

    @Override
    public void removeClassName(String className) {
        button.getStyleClass().remove(className);
    }

}

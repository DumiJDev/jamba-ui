package io.github.dumijdev.jambaui.desktop.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.layouts.AbstractVerticalLayout;
import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class VerticalLayout extends AbstractVerticalLayout<VBox> {
    private final VBox vbox;

    public VerticalLayout() {
        vbox = new VBox();
        this.style = new ComponentStyle(vbox);
    }

    @Override
    public void add(Component<?>... component) {
        for (Component<?> c : component) {
            vbox.getChildren().add((Node) c.getInternal());
        }
    }

    @Override
    public void remove(Component<?> component) {
        vbox.getChildren().remove((Node) component.getInternal());
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public void update() {
        vbox.layout();
    }

    @Override
    public VBox getInternal() {
        return vbox;
    }

    @Override
    public void addClassName(String className) {
        vbox.getStyleClass().add(className);
    }

    @Override
    public void removeClassName(String className) {
        vbox.getStyleClass().remove(className);
    }
}

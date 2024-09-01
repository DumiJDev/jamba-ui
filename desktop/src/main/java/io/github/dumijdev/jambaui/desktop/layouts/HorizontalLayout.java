package io.github.dumijdev.jambaui.desktop.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Style;
import io.github.dumijdev.jambaui.core.layouts.AbstractHorizontalLayout;
import io.github.dumijdev.jambaui.core.layouts.Layout;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class HorizontalLayout extends AbstractHorizontalLayout<HBox> {
    private final HBox hbox = new HBox();

    public HorizontalLayout() {

    }

    @Override
    public Layout<HBox> copy() {
        return this;
    }

    @Override
    public void add(Component<?> component) {
        hbox.getChildren().add(((Component<Node>) component).getInternal());
    }

    @Override
    public void remove(Component<?> component) {

    }

    @Override
    public Style getStyle() {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) {

    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public void update() {
        hbox.layout();
    }

    @Override
    public HBox getInternal() {
        return hbox;
    }
}

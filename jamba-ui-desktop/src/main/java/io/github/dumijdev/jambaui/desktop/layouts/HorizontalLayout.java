package io.github.dumijdev.jambaui.desktop.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.layouts.AbstractHorizontalLayout;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class HorizontalLayout extends AbstractHorizontalLayout<HBox> {
    private final HBox hbox;
    private final Map<String, Object> properties;

    public HorizontalLayout() {
        this.hbox = new HBox();
        this.properties = new HashMap<>();
        this.style = new ComponentStyle(hbox)
                .set("-alignment", "center-left");
    }

    @Override
    public void add(Component<?>... component) {
        for (Component<?> c : component) {
            var child = ((Component<Node>) c).getInternal();

            if (!hbox.getChildren().contains(child)) {
                hbox.getChildren().add(child);
            }
        }
    }

    @Override
    public void remove(Component<?> component) {
        hbox.getChildren().remove((Node) component.getInternal());
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public void update() {
        hbox.layout();
    }

    @Override
    public HBox getInternal() {
        return hbox;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorizontalLayout that)) return false;
        if (!super.equals(o)) return false;

        return hbox.equals(that.hbox) && properties.equals(that.properties) && super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + hbox.hashCode();
        result = 31 * result + properties.hashCode();
        return result;
    }

    @Override
    public void addClassName(String className) {
        hbox.getStyleClass().add(className);
    }

    @Override
    public void removeClassName(String className) {
        hbox.getStyleClass().remove(className);
    }
}

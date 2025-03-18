package io.github.dumijdev.jambaui.desktop.layouts;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.layouts.AbstractAppLayout;
import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AppLayout extends AbstractAppLayout<VBox> {
    private final VBox root;
    private final HorizontalLayout header;
    private final VerticalLayout sideBar;
    private final AnchorPane content;

    public AppLayout() {
        root = new VBox();
        header = new HorizontalLayout();
        sideBar = new VerticalLayout();
        content = new AnchorPane();
        this.style = new ComponentStyle(root);

        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);

        var hContainer = new HBox();
        hContainer.getChildren().add(sideBar.getInternal());
        hContainer.getChildren().add(content);

        root.getChildren().add(header.getInternal());

        root.getChildren().add(hContainer);

    }

    @Override
    public void addToSideBar(Component<?>... component) {
        sideBar.add(component);
    }

    @Override
    public void addToHeader(Component<?> component) {
        header.add(component);
    }

    @Override
    public void add(Component<?>... component) {
        for (Component<?> c : component) {
            content.getChildren().add((Node) c.getInternal());
        }
    }

    @Override
    public void remove(Component<?> component) {
        content.getChildren().remove((Node) component.getInternal());
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public void update() {
        root.layout();
    }

    @Override
    public VBox getInternal() {
        return root;
    }

    @Override
    public void addClassName(String className) {
        root.getStyleClass().add(className);
    }

    @Override
    public void removeClassName(String className) {
        root.getStyleClass().remove(className);
    }
}

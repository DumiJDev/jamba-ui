package io.github.dumijdev.jambaui.desktop;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.ioc.container.ViewContainer;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class UIManager {
    private static final Pane parent = new AnchorPane();

    static {
        AnchorPane.setTopAnchor(parent, 0.0);
        AnchorPane.setLeftAnchor(parent, 0.0);
        AnchorPane.setRightAnchor(parent, 0.0);
        AnchorPane.setBottomAnchor(parent, 0.0);
    }

    @SuppressWarnings("unchecked")
    public static void navigate(String viewName) {
        var view = (Component<Node>) ViewContainer.getInstance().resolve(viewName);

        if (view == null) {
            throw new RuntimeException("Unable to find view: " + viewName);
        }

        activeView(view);
    }

    static void activeView(Component<Node> view) {
        parent.getChildren().clear();
        parent.getChildren().add(view.getInternal());
    }

    static Pane getRoot() {
        return parent;
    }
}

package io.github.dumijdev.jambaui.desktop.utils;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.ioc.annotations.Injectable;
import io.github.dumijdev.jambaui.ioc.container.ViewContainer;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Injectable
public class Navigator {
    private static final Pane parent = new AnchorPane();
    private static final Stage stage = new Stage();

    public Navigator() {
    }

    static {
        AnchorPane.setTopAnchor(parent, 0.0);
        AnchorPane.setLeftAnchor(parent, 0.0);
        AnchorPane.setRightAnchor(parent, 0.0);
        AnchorPane.setBottomAnchor(parent, 0.0);
    }

    public void navigate(String viewName) {
        var screen = ViewContainer.getInstance().resolve(viewName);

        if (screen == null) {
            ViewContainer.getInstance().registerFromBase(ViewContainer.getInstance().getMainView().getClass());

            screen = ViewContainer.getInstance().resolve(viewName);
            if (screen == null) {
                throw new RuntimeException("Unable to find view: " + viewName);
            }
        }

        try {
            Thread.sleep(500);
            activeView((Component<Node>) screen.component());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void activeView(Component<Node> view) {
        parent.getChildren().clear();
        parent.getChildren().add(view.getInternal());
    }

    public Pane getRoot() {
        return parent;
    }

    public Component<Node> getMain() {
        return (Component<Node>) ViewContainer.getInstance().getMainView();
    }

    public Stage getStage() {
        return stage;
    }
}

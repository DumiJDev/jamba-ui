package io.github.dumijdev.jambaui.desktop.utils;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Injectable;
import io.github.dumijdev.jambaui.core.utils.Filter;
import io.github.dumijdev.jambaui.core.utils.FilterChain;
import io.github.dumijdev.jambaui.core.utils.NavigationContext;
import io.github.dumijdev.jambaui.core.utils.Navigator;
import io.github.dumijdev.jambaui.desktop.exceptions.ViewNotFoundException;
import io.github.dumijdev.jambaui.context.container.ViewContainer;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Injectable
public class UINavigator implements Navigator {
    private static final Pane parent = new AnchorPane();
    private static Stage stage;
    private final List<Filter> filters;
    private String currentView;

    public UINavigator() {
        filters = new LinkedList<>();
    }

    static {
        AnchorPane.setTopAnchor(parent, 0.0);
        AnchorPane.setLeftAnchor(parent, 0.0);
        AnchorPane.setRightAnchor(parent, 0.0);
        AnchorPane.setBottomAnchor(parent, 0.0);
    }

    private void activeView(Screen<?, ?> screen) {
        var view = (Component<Node>) screen.component();

        parent.getChildren().clear();
        parent.getChildren().add(view.getInternal());
        System.out.println("Current title: " + screen.layout().getStyle().getTitle());
        stage.setTitle(screen.layout().getStyle().getTitle());
    }

    public Pane getRoot() {
        return parent;
    }

    public Screen<?, ?> getMain() {
        return ViewContainer.getInstance().getMainView();
    }

    public Stage getStage() {
        if (stage == null) {
            stage = new Stage();
        }

        return stage;
    }

    @Override
    public void navigateTo(String path) {
        var screen = ViewContainer.getInstance().resolve(path);

        if (screen == null) {
            ViewContainer.getInstance().registerFromBase(ViewContainer.getInstance().getMainView().getClass());

            screen = ViewContainer.getInstance().resolve(path);
            if (screen == null) {
                throw new ViewNotFoundException("Unable to find view: " + path);
            }
        }

        var filterChain = new FilterChain(filters);

        NavigationContext.getInstance().setFromView(currentView);
        NavigationContext.getInstance().setToView(path);

        filterChain.doFilter(NavigationContext.getInstance());

        currentView = path;

        activeView(screen);
    }

    @Override
    public void addFilter(Filter filter) {
        filters.add(filter);
    }
}

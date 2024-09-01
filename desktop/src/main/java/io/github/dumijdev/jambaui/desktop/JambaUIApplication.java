package io.github.dumijdev.jambaui.desktop;

import com.sun.javafx.application.PlatformImpl;
import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.ioc.container.IoCContainer;
import io.github.dumijdev.jambaui.ioc.container.LayoutContainer;
import io.github.dumijdev.jambaui.ioc.container.LogicContainer;
import io.github.dumijdev.jambaui.ioc.container.ViewContainer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class JambaUIApplication {
    private static final List<IoCContainer<?, ?>> containers;

    static {
        containers = new LinkedList<>();

        containers.add(LogicContainer.getInstance());
        containers.add(LayoutContainer.getInstance());
        containers.add(ViewContainer.getInstance());
    }

    private static void start(Stage stage) throws Exception {

        var main = (Component<Node>) ViewContainer.getInstance().getMainView();

        var root = UIManager.getRoot();

        root.getChildren().add(main.getInternal());

        stage.setScene(new Scene(root, 700, 700));

        stage.show();

    }

    public static void run(Class<?> initClass, String... args) {

        PlatformImpl.startup(() -> {
            for (var container : containers) {
                container.registerFromBase(initClass);
            }
            try {
                start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    public static void registerContainer(IoCContainer<?, ?> container) {
        containers.add(container);
    }

}

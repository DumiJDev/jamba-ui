package io.github.dumijdev.jambaui.desktop;

import com.sun.javafx.application.PlatformImpl;
import io.github.dumijdev.jambaui.core.annotations.OnCreated;
import io.github.dumijdev.jambaui.core.utils.Navigator;
import io.github.dumijdev.jambaui.desktop.utils.UINavigator;
import io.github.dumijdev.jambaui.context.container.ApplicationContainer;
import io.github.dumijdev.jambaui.context.container.IoCContainer;
import io.github.dumijdev.jambaui.context.factory.LayoutFactory;
import io.github.dumijdev.jambaui.context.container.ViewContainer;
import io.github.dumijdev.jambaui.context.utils.ConfigurationManager;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class JambaUIApplication {
    private static final List<IoCContainer<?, ?>> containers;
    private static UINavigator navigator = new UINavigator();

    static {
        containers = new LinkedList<>();

        containers.add(ApplicationContainer.getInstance());
        containers.add(ViewContainer.getInstance());
    }

    private static void start() {
        var main = navigator.getMain();

        var root = navigator.getRoot();

        var stage = navigator.getStage();

        stage.setTitle(main.layout().getStyle().getTitle());

        root.getChildren().add((Node) main.component().getInternal());

        stage.setScene(new Scene(root, 700, 700));

        stage.show();

    }

    public static void run(Class<?> initClass, String... args) {

        PlatformImpl.startup(() -> {
            ApplicationContainer.getInstance().register(Navigator.class, navigator);
            ApplicationContainer.getInstance().register(ConfigurationManager.class, ConfigurationManager.getInstance());
            for (var container : containers) {
                System.out.println("========================================");
                System.out.println("Running " + container.getClass().getSimpleName());
                container.registerFromBase(initClass);
                System.out.println("========================================\b");
                System.out.println("Ending " + container.getClass().getSimpleName());
            }

            start();

            for (var container : containers) {
                for (var obj : container.resolveAll()) {
                    invokeOnCreated(obj);
                }
            }

        });
    }

    public static void registerContainer(IoCContainer<?, ?> container) {
        containers.add(container);
    }

    private static void invokeOnCreated(Object instance) {
        var methods = instance.getClass().getDeclaredMethods();
        try {
            for (var method : methods) {
                if (method.isAnnotationPresent(OnCreated.class)) {
                    method.setAccessible(true);
                    method.invoke(instance);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke annotated method", e);
        }
    }


}

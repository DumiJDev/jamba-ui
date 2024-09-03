package io.github.dumijdev.jambaui.desktop;

import com.sun.javafx.application.PlatformImpl;
import io.github.dumijdev.jambaui.desktop.utils.Navigator;
import io.github.dumijdev.jambaui.ioc.annotations.OnCreated;
import io.github.dumijdev.jambaui.ioc.container.ApplicationContainer;
import io.github.dumijdev.jambaui.ioc.container.IoCContainer;
import io.github.dumijdev.jambaui.ioc.container.LayoutContainer;
import io.github.dumijdev.jambaui.ioc.container.ViewContainer;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class JambaUIApplication {
    private static final List<IoCContainer<?, ?>> containers;

    static {
        containers = new LinkedList<>();

        containers.add(ApplicationContainer.getInstance());
        containers.add(LayoutContainer.getInstance());
        containers.add(ViewContainer.getInstance());
    }

    private static void start() {

        var navigator = new Navigator();

        var main = navigator.getMain();

        var root = navigator.getRoot();

        var stage = navigator.getStage();

        root.getChildren().add(main.getInternal());

        stage.setScene(new Scene(root, 700, 700));

        stage.show();

    }

    public static void run(Class<?> initClass, String... args) {

        PlatformImpl.startup(() -> {
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
        System.out.println("Class name: " + instance.getClass().getSimpleName());

        try {
            for (var method : methods) {
                System.out.println("Method: " + method.getName());
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

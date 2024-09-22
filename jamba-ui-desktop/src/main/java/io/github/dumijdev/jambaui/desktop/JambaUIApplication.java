package io.github.dumijdev.jambaui.desktop;

import com.sun.javafx.application.PlatformImpl;
import io.github.dumijdev.jambaui.context.container.ApplicationContainer;
import io.github.dumijdev.jambaui.context.container.ExceptionHandlerContainer;
import io.github.dumijdev.jambaui.context.container.IoCContainer;
import io.github.dumijdev.jambaui.context.container.ViewContainer;
import io.github.dumijdev.jambaui.context.utils.BannerUtils;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.annotations.OnCreated;
import io.github.dumijdev.jambaui.core.annotations.OnDestroy;
import io.github.dumijdev.jambaui.desktop.utils.UINavigator;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class JambaUIApplication {

    private static final List<IoCContainer<?, ?>> containers = new LinkedList<>();
    private static boolean isRunning = false;
    private static final Logger logger = LoggerFactory.getLogger(JambaUIApplication.class);

    static {
        containers.add(ApplicationContainer.getInstance());
        containers.add(ViewContainer.getInstance());
    }

    public static void run(Class<?> initClass, String... args) {
        if (!isRunning) {

            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                ExceptionHandlerContainer.getInstance().handleException(throwable);
            });

            PlatformImpl.startup(new ApplicationRunner(initClass, args));
        } else {
            throw new IllegalStateException("Application is already running.");
        }
    }

    public static void registerContainer(IoCContainer<?, ?> container) {
        containers.add(container);
    }

    // Load components and register them in containers
    private static void loadComponents(Class<?> initClass) {
        scanAndRegisterComponents(initClass);
    }

    private static void scanAndRegisterComponents(Class<?>... classes) {
        for (var clazz : classes) {
            registerClass(clazz);
        }
    }

    private static void registerClass(Class<?> clazz) {
        for (IoCContainer<?, ?> container : containers) {
            container.registerFromBase(clazz);
        }
    }

    // Application startup logic
    private static void startup() {
        logger.info("Application startup...");
        runOnCreated();
    }

    // Main application execution logic
    private static void runApp() {
        var navigator = (UINavigator) ApplicationContainer.getInstance().resolve(UINavigator.class);
        Screen<?, ?> main = navigator.getMain();
        var root = navigator.getRoot();
        var stage = navigator.getStage();

        stage.setOnCloseRequest(JambaUIApplication::onCloseRequest);

        stage.setTitle(main.layout().getStyle().getTitle());
        root.getChildren().add((Node) main.component().getInternal());

        stage.setScene(new Scene(root, 700, 700));
        stage.show();

        logger.info("Application is now running...");
    }

    // Run destroy hooks when the application is shutting down
    private static void runOnDestroy() {
        logger.info("Application is shutting down...");
        for (var container : containers) {
            for (var obj : container.resolveAll()) {
                invokeOnDestroy(obj);
            }
        }
    }

    private static void invokeOnDestroy(Object instance) {
        var methods = instance.getClass().getDeclaredMethods();
        try {
            for (var method : methods) {
                if (method.isAnnotationPresent(OnDestroy.class)) {
                    method.setAccessible(true);
                    method.invoke(instance);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke @OnDestroy method in " + instance.getClass().getName(), e);
        }
    }

    // Invoking methods annotated with @OnCreated
    private static void runOnCreated() {
        for (var container : containers) {
            for (var obj : container.resolveAll()) {
                invokeOnCreated(obj);
            }
        }
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
            throw new RuntimeException("Failed to invoke @OnCreated method in " + instance.getClass().getName(), e);
        }
    }

    // Exit application gracefully
    private static void exit() {
        runOnDestroy();
        PlatformImpl.exit();
        logger.info("Application exited.");
    }

    private static void onCloseRequest(WindowEvent windowEvent) {
        runOnDestroy();
        logger.info("Application exited.");
    }

    // Application lifecycle runner
    private record ApplicationRunner(Class<?> initClass, String[] args) implements Runnable {
        private static final ExceptionHandlerContainer exContainer = ExceptionHandlerContainer.getInstance();


        @Override
        public void run() {
            try {
                isRunning = true;
                BannerUtils.printBanner();

                long start = System.currentTimeMillis();

                loadComponents(initClass);

                runApp();

                startup();

                long end = System.currentTimeMillis();
                double time = (double) (end - start) / 1000;

                logger.info("Application initialized in {} s", time);

            } catch (Exception e) {
                exContainer.handleException(e);
                exit();
            }
        }
    }
}

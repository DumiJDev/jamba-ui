package io.github.dumijdev.jambaui.desktop;

import com.sun.javafx.application.PlatformImpl;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.dumijdev.jambaui.context.container.ApplicationContainer;
import io.github.dumijdev.jambaui.context.container.IoCContainer;
import io.github.dumijdev.jambaui.context.container.ViewContainer;
import io.github.dumijdev.jambaui.context.utils.BannerUtils;
import io.github.dumijdev.jambaui.context.utils.ConfigurationManager;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.annotations.Injectable;
import io.github.dumijdev.jambaui.core.annotations.OnCreated;
import io.github.dumijdev.jambaui.core.utils.Navigator;
import io.github.dumijdev.jambaui.desktop.utils.UINavigator;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class JambaUIApplication {

    private static final List<IoCContainer<?, ?>> containers = new LinkedList<>();
    private static final UINavigator navigator = new UINavigator();

    static {
        containers.add(ApplicationContainer.getInstance());
        containers.add(ViewContainer.getInstance());
    }

    public static void run(Class<?> initClass, String... args) {
        PlatformImpl.startup(new ApplicationRunner(initClass, args));
    }

    public static void registerContainer(IoCContainer<?, ?> container) {
        containers.add(container);
    }

    private static void init(Class<?> initClass) {
        scanAndRegisterComponents(initClass.getPackage().getName());
    }

    private static void scanAndRegisterComponents(String... packages) {
        for (String pkg : packages) {
            try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(pkg).scan()) {

                for (ClassInfo classInfo : scanResult.getAllClasses()) {
                    registerClass(classInfo.loadClass());
                }
            }
        }
    }

    private static void registerClass(Class<?> clazz) {
        for (IoCContainer<?, ?> container : containers) {
            container.registerFromBase(clazz);
        }
    }

    private static void start() {
        Screen<?, ?> main = navigator.getMain();
        var root = navigator.getRoot();
        var stage = navigator.getStage();

        stage.setTitle(main.layout().getStyle().getTitle());
        root.getChildren().add((Node) main.component().getInternal());

        stage.setScene(new Scene(root, 700, 700));
        stage.show();
    }

    private static void runOnCreated() {
        // Invocar todos os métodos anotados com @OnCreated
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

    private static class ApplicationRunner implements Runnable {
        private final Class<?> initClass;
        private final String[] args;

        private ApplicationRunner(Class<?> initClass, String[] args) {
            this.initClass = initClass;
            this.args = args;
        }

        @Override
        public void run() {
            try {
                BannerUtils.printBanner();
                var start = System.currentTimeMillis();
                init(initClass);
                start();
                runOnCreated();
                var end = System.currentTimeMillis();

                System.out.println(end - start + " ms");
            } catch (Exception e) {
                e.printStackTrace();
                PlatformImpl.exit();
            }
        }
    }
}

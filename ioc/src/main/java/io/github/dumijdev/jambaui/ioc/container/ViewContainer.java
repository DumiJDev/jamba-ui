package io.github.dumijdev.jambaui.ioc.container;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.components.DefaultScreen;
import io.github.dumijdev.jambaui.core.layouts.Layout;
import io.github.dumijdev.jambaui.ioc.annotations.Inject;
import io.github.dumijdev.jambaui.ioc.annotations.OnCreated;
import io.github.dumijdev.jambaui.ioc.annotations.View;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewContainer implements IoCContainer<String, Screen<?, ?>> {
    private final Map<String, Screen<?, ?>> views = new HashMap<>();
    private static final ViewContainer instance = new ViewContainer();
    private Component<?> main = null;

    public static ViewContainer getInstance() {
        return instance;
    }

    private ViewContainer() {
    }

    public void registerFromBase(Class<?> rootClass) {
        var reflections = new Reflections(rootClass);
        LayoutContainer.getInstance().registerFromBase(rootClass);

        var candidateViews = reflections.getTypesAnnotatedWith(View.class);

        for (var candidate : candidateViews) {
            if (!Modifier.isAbstract(candidate.getModifiers()) && !candidate.isInterface()) {
                createInjectable(candidate);
            }
        }

    }

    public Screen<?, ?> resolve(String s) {
        return views.get(s);
    }

    public void register(String s, Screen<?, ?> screen) {
        views.put(s, screen);
    }

    @Override
    public List<Screen<?, ?>> resolveAll() {
        return views.values().stream().toList();
    }

    private void createInjectable(Class<?> candidate) {
        try {
            var view = candidate.getAnnotation(View.class);

            if (views.containsKey(view.value())) {
                return;
            }

            System.out.println("View name: " + view.value());

            var contructors = candidate.getDeclaredConstructors();
            Constructor<?> injectConstructor = null;

            for (var constructor : contructors) {
                if (constructor.isAnnotationPresent(Inject.class)) {
                    injectConstructor = constructor;
                    break;
                }
            }

            Object instance;
            if (injectConstructor != null) {

                Class<?>[] parameterTypes = injectConstructor.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters[i] = getInjectable(parameterTypes[i]);
                }

                instance = injectConstructor.newInstance(parameters);
            } else {
                instance = candidate.getDeclaredConstructor().newInstance();
            }

            var layout = LayoutContainer.getInstance().resolve(view.layout());
            var isMain = view.main();

            if (layout == null && view.layout() != null) {
                LayoutContainer.getInstance().registerFromBase(view.layout());
                layout = LayoutContainer.getInstance().resolve(view.layout());
            }

            if (layout == null) {
                throw new RuntimeException("Could not resolve layout for view " + candidate);
            }

            var layoutClone = layout.copy();

            layoutClone.add((Component<?>) instance);

            register(view.value(), new DefaultScreen<>((Layout<Object>) layoutClone, (Component<Object>) instance));

            if (isMain) {
                if (main == null) {
                    main = (Component<?>) instance;
                } else {
                    throw new RuntimeException("Cannot register main view " + candidate);
                }
            }

            injectFields(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectFields(Object instance) {
        Class<?> beanClass = instance.getClass();
        var fields = beanClass.getDeclaredFields();

        for (var field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                System.out.println("View: " + field.getName());
                try {
                    if (field.trySetAccessible()) {
                        Object dependency = getInjectable(field.getType());
                        System.out.println("Inject: " + field.getName());
                        System.out.println("Dependency: " + dependency);
                        field.set(instance, dependency);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    private <T> T getInjectable(Class<T> beanClass) {
        T bean = (T) ApplicationContainer.getInstance().resolve(beanClass);
        if (bean == null) {
            ApplicationContainer.getInstance().registerFromBase(beanClass);
            bean = (T) ApplicationContainer.getInstance().resolve(beanClass);
            System.out.println("Inject: " + bean + ", type: " + beanClass.getSimpleName());
        }

        return bean;
    }

    public Component<?> getMainView() {
        if (main == null) {
            throw new RuntimeException("No main view found");
        }

        return main;
    }
}

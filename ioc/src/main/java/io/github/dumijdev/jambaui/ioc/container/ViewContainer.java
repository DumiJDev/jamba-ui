package io.github.dumijdev.jambaui.ioc.container;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.ioc.annotations.Inject;
import io.github.dumijdev.jambaui.ioc.annotations.View;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ViewContainer implements IoCContainer<String, Component<?>> {
    private final Map<String, Component<?>> views = new HashMap<>();
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

    public Component<?> resolve(String s) {
        return views.get(s);
    }

    public void register(String s, Component<?> component) {
        views.put(s, component);
    }

    private void createInjectable(Class<?> candidate) {
        try {
            var view = candidate.getAnnotation(View.class);
            var instance = candidate.getDeclaredConstructor().newInstance();
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

            register(view.value(), layoutClone);

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
                try {
                    if (field.trySetAccessible()) {
                        Object dependency = getInjectable(field.getType());
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
        T bean = (T) LogicContainer.getInstance().resolve(beanClass);
        if (bean == null) {
            LogicContainer.getInstance().registerFromBase(beanClass);
            bean = (T) LogicContainer.getInstance().resolve(beanClass);
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

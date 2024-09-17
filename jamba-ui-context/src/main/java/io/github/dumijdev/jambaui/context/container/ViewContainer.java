package io.github.dumijdev.jambaui.context.container;

import io.github.classgraph.ClassGraph;
import io.github.dumijdev.jambaui.context.factory.LayoutFactory;
import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Property;
import io.github.dumijdev.jambaui.core.annotations.View;
import io.github.dumijdev.jambaui.core.components.DefaultScreen;
import io.github.dumijdev.jambaui.core.layouts.Layout;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewContainer implements IoCContainer<String, Screen<?, ?>> {
    private final Map<String, Screen<?, ?>> views = new HashMap<>();
    private static final ViewContainer instance = new ViewContainer();
    private Screen<?, ?> main = null;

    public static ViewContainer getInstance() {
        return instance;
    }

    private ViewContainer() {
    }

    public void registerFromBase(Class<?> rootClass) {
        try (var result = new ClassGraph().enableAllInfo().acceptPackages(rootClass.getPackage().getName()).scan()) {

            for (var candidate : result.getClassesWithAnnotation(View.class)) {
                var clazz = candidate.loadClass();
                if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                    createInjectable(clazz);
                }
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
            var view = candidate.getDeclaredAnnotation(View.class);

            if (views.containsKey(view.value())) {
                return;
            }

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

                var parameters = injectConstructor.getParameters();
                var parameterTypes = injectConstructor.getParameterTypes();
                Object[] parameterValues = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    if (parameters[i].isAnnotationPresent(Property.class)) {
                        var property = parameters[i].getDeclaredAnnotation(Property.class);
                        parameterValues[i] = PropertyResolver.resolve(property.value(), parameterTypes[i]);
                    } else {
                        parameterValues[i] = getInjectable(parameterTypes[i]);
                    }
                }

                instance = injectConstructor.newInstance(parameterValues);
            } else {
                instance = candidate.getDeclaredConstructor().newInstance();
            }

            var layout = LayoutFactory.getInstance().resolve(view.layout());
            var isMain = view.main();
            var title = view.title();
            var value = view.value();

            if (layout == null) {
                throw new RuntimeException("Could not resolve layout for view " + candidate);
            }

            layout.getStyle().setTitle(PropertyResolver.resolve(title.isEmpty() ? value : title));
            layout.add((Component<?>) instance);

            var screen = new DefaultScreen<>((Layout<Object>) layout, (Component<Object>) instance);

            register(view.value(), screen);

            if (isMain) {
                if (main == null) {
                    main = screen;
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
            if (field.trySetAccessible()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    try {
                        Object dependency = getInjectable(field.getType());
                        field.set(instance, dependency);

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else if (field.isAnnotationPresent(Property.class)) {

                    try {
                        var property = field.getAnnotation(Property.class);
                        field.set(instance, PropertyResolver.resolve(property.value(), field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

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
        }

        return bean;
    }

    public Screen<?, ?> getMainView() {
        if (main == null) {
            throw new RuntimeException("No main view found");
        }

        return main;
    }
}

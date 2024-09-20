package io.github.dumijdev.jambaui.context.container;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.dumijdev.jambaui.context.factory.LayoutFactory;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;
import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.Screen;
import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Property;
import io.github.dumijdev.jambaui.core.annotations.View;
import io.github.dumijdev.jambaui.core.components.DefaultScreen;
import io.github.dumijdev.jambaui.core.layouts.Layout;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewContainer implements IoCContainer<String, Screen<?, ?>> {
    private final Map<String, Screen<?, ?>> views = new HashMap<>();
    private static final ViewContainer instance = new ViewContainer();
    private Screen<?, ?> main = null;

    private ViewContainer() {
    }

    public static ViewContainer getInstance() {
        return instance;
    }

    public void registerFromBase(Class<?> rootClass) {
        try (var result = new ClassGraph().enableAllInfo().acceptPackages(rootClass.getPackage().getName()).scan()) {
            readDependencies(result);
        } catch (Exception e) {
            throw new RuntimeException("Error scanning package: " + rootClass.getPackage().getName(), e);
        }
    }

    private void readDependencies(ScanResult result) {

        result.getClassesWithAnnotation(View.class).stream()
                .map(ClassInfo::loadClass)
                .filter(this::isInstantiable)
                .forEach(this::createInjectable);

    }

    private boolean isInstantiable(Class<?> clazz) {
        return !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface();
    }

    @Override
    public Screen<?, ?> resolve(String s) {
        return views.get(s);
    }

    @Override
    public void register(String s, Screen<?, ?> screen) {
        views.put(s, screen);
    }

    @Override
    public List<Screen<?, ?>> resolveAll() {
        return new LinkedList<>(views.values());
    }

    private void createInjectable(Class<?> candidate) {
        try {
            var viewAnnotation = candidate.getDeclaredAnnotation(View.class);

            if (views.containsKey(viewAnnotation.value())) {
                return;
            }

            Object instance = createInstance(candidate);

            Layout<?> layout = resolveLayout(viewAnnotation.layout(), candidate);
            layout.getStyle().setTitle(PropertyResolver.resolve(viewAnnotation.title().isEmpty() ? viewAnnotation.value() : viewAnnotation.title()));
            layout.add((Component<?>) instance);

            Screen<?, ?> screen = new DefaultScreen<>((Layout<Object>) layout, (Component<Object>) instance);
            register(viewAnnotation.value(), screen);

            if (viewAnnotation.main()) {
                setMainView(candidate, screen);
            }

            injectFields(instance);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create view for class: " + candidate.getName(), e);
        }
    }

    private Layout<?> resolveLayout(Class<?> layoutClass, Class<?> viewClass) {
        Layout<?> layout = LayoutFactory.getInstance().resolve((Class<? extends Layout<?>>) layoutClass);
        if (layout == null) {
            throw new RuntimeException("Could not resolve layout for view: " + viewClass.getName());
        }
        return layout;
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> injectConstructor = findInjectConstructor(clazz);

        if (injectConstructor != null) {
            Object[] parameterValues = resolveConstructorParameters(injectConstructor);
            return injectConstructor.newInstance(parameterValues);
        } else {
            return clazz.getDeclaredConstructor().newInstance();
        }
    }

    private Constructor<?> findInjectConstructor(Class<?> clazz) {
        for (var constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return null;
    }

    private Object[] resolveConstructorParameters(Constructor<?> constructor) throws Exception {
        var parameters = constructor.getParameters();
        Object[] parameterValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            var param = parameters[i];
            parameterValues[i] = param.isAnnotationPresent(Property.class) ?
                    PropertyResolver.resolve(param.getAnnotation(Property.class).value(), param.getType()) :
                    getInjectable(param.getType());
        }

        return parameterValues;
    }

    private void injectFields(Object instance) {
        Class<?> clazz = instance.getClass();

        for (var field : clazz.getDeclaredFields()) {
            if (field.trySetAccessible()) {
                try {
                    if (field.isAnnotationPresent(Inject.class)) {
                        Object dependency = getInjectable(field.getType());
                        field.set(instance, dependency);
                    } else if (field.isAnnotationPresent(Property.class)) {
                        var property = field.getAnnotation(Property.class);
                        field.set(instance, PropertyResolver.resolve(property.value(), field.getType()));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject field: " + field.getName(), e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getInjectable(Class<T> clazz) {
        return (T) ApplicationContainer.getInstance().resolve(clazz);
    }

    private void setMainView(Class<?> viewClass, Screen<?, ?> screen) {
        if (main == null) {
            main = screen;
        } else {
            throw new RuntimeException("Multiple main views found: " + viewClass.getName());
        }
    }

    public Screen<?, ?> getMainView() {
        if (main == null) {
            throw new RuntimeException("No main view registered");
        }
        return main;
    }
}


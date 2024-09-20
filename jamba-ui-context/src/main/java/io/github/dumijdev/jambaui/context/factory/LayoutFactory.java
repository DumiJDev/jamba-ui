package io.github.dumijdev.jambaui.context.factory;

import io.github.dumijdev.jambaui.context.container.ApplicationContainer;
import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Property;
import io.github.dumijdev.jambaui.core.layouts.Layout;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class LayoutFactory {
    private static final LayoutFactory instance = new LayoutFactory();

    public static LayoutFactory getInstance() {
        return instance;
    }

    private LayoutFactory() {
    }

    private Layout<?> createInjectable(Class<? extends Layout<?>> type) {
        try {
            // Obter o construtor com a anotação @Inject, se houver
            Constructor<?> injectConstructor = findInjectableConstructor(type);

            // Instanciar a classe com ou sem parâmetros injetáveis
            Object instance = (injectConstructor != null)
                    ? instantiateWithDependencies(injectConstructor)
                    : type.getDeclaredConstructor().newInstance();

            return (Layout<?>) instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create an instance of layout: " + type.getName(), e);
        }
    }

    public Layout<?> resolve(Class<? extends Layout<?>> layoutClass) {
        return createInjectable(layoutClass);
    }

    private Constructor<?> findInjectableConstructor(Class<?> clazz) {
        for (var constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return null;
    }

    private Object instantiateWithDependencies(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        var parameters = constructor.getParameters();
        var parameterTypes = constructor.getParameterTypes();
        Object[] parameterValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameterValues[i] = resolveParameter(parameters[i], parameterTypes[i]);
        }

        return constructor.newInstance(parameterValues);
    }

    private Object resolveParameter(Parameter parameter, Class<?> parameterType) {
        if (parameter.isAnnotationPresent(Property.class)) {
            var property = parameter.getDeclaredAnnotation(Property.class);
            return PropertyResolver.resolve(property.value(), parameterType);
        } else {
            return getInjectable(parameterType);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getInjectable(Class<T> beanClass) {
        return (T) ApplicationContainer.getInstance().resolve(beanClass);
    }
}

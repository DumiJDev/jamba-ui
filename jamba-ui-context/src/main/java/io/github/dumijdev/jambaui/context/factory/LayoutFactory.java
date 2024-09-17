package io.github.dumijdev.jambaui.context.factory;

import io.github.dumijdev.jambaui.context.container.ApplicationContainer;
import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Property;
import io.github.dumijdev.jambaui.core.layouts.Layout;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class LayoutFactory {
    private static final LayoutFactory instance = new LayoutFactory();

    public static LayoutFactory getInstance() {
        return instance;
    }

    private LayoutFactory() {
    }

    private Layout<?> createInjectable(Class<? extends Layout<?>> type) {
        try {
            var contructors = type.getDeclaredConstructors();
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
                        System.out.println(property.value());
                        parameterValues[i] = PropertyResolver.resolve(property.value(), parameterTypes[i]);
                    } else {
                        parameterValues[i] = getInjectable(parameterTypes[i]);
                    }
                }

                instance = injectConstructor.newInstance(parameterValues);
            } else {
                instance = type.getDeclaredConstructor().newInstance();
            }

            return (Layout<?>) instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Layout<?> resolve(Class<? extends Layout<?>> c) {
        return createInjectable(c);
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
}

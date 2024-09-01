package io.github.dumijdev.jambaui.ioc.container;

import io.github.dumijdev.jambaui.ioc.annotations.Inject;
import io.github.dumijdev.jambaui.ioc.annotations.Logic;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class LogicContainer implements IoCContainer<Class<?>, Object> {
    private final Map<Class<?>, Object> objects = new HashMap<>();
    private static final LogicContainer instance = new LogicContainer();

    public static LogicContainer getInstance() {
        return instance;
    }

    private LogicContainer() {
    }


    @Override
    public void registerFromBase(Class<?> rootClass) {
        var reflections = new Reflections(rootClass);

        var candidates = reflections.getTypesAnnotatedWith(Logic.class);

        for (var candidate : candidates) {
            if (!Modifier.isAbstract(candidate.getModifiers()) && !candidate.isInterface()) {
                createInjectable(candidate);
            }
        }
    }

    private void createInjectable(Class<?> candidate) {
        try {
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

            register(candidate, instance);

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
                    field.setAccessible(true);
                    Object dependency = getInjectable(field.getType());
                    field.set(instance, dependency);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object resolve(Class<?> aClass) {
        if (!objects.containsKey(aClass)) {
            registerFromBase(aClass);
        }

        return objects.get(aClass);
    }

    @Override
    public void register(Class<?> aClass, Object o) {
        objects.put(aClass, o);
    }

    private <T> T getInjectable(Class<T> beanClass) {
        T injectable = (T) objects.get(beanClass);
        if (injectable == null) {
            createInjectable(beanClass);
            injectable = (T) objects.get(beanClass);
        }
        return injectable;
    }
}

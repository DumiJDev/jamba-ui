package io.github.dumijdev.jambaui.context.container;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;
import io.github.dumijdev.jambaui.core.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class ApplicationContainer implements IoCContainer<Class<?>, Object> {
    private final Map<Class<?>, Object> objects = new HashMap<>();
    private static final ApplicationContainer instance = new ApplicationContainer();
    private final Logger logger = LoggerFactory.getLogger(ApplicationContainer.class);

    private ApplicationContainer() {
        objects.put(ApplicationContainer.class, this);
    }

    public static ApplicationContainer getInstance() {
        return instance;
    }

    @Override
    public void registerFromBase(Class<?> rootClass) {
        try (var result = new ClassGraph().enableAllInfo().acceptPackages(rootClass.getPackage().getName()).scan()) {
            readDependencies(result);
        }

        try (var result = new ClassGraph().enableAllInfo().acceptPackages("io.github.dumijdev.jambaui").scan()) {
            readDependencies(result);
        }
    }

    private void readDependencies(ScanResult result) {
        for (var candidate : result.getClassesWithAnnotation(Injectable.class)) {
            var clazz = candidate.loadClass();
            if (clazz.isAnnotation()) {

            } else if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                createInjectable(clazz);
            }
        }

        for (var candidate : result.getClassesWithAnnotation(Logic.class)) {
            if (!Modifier.isAbstract(candidate.getModifiers()) && !candidate.isInterface()) {
                createInjectable(candidate.loadClass());
            }
        }

        for (var candidate : result.getClassesWithAnnotation(Config.class)) {
            if (!Modifier.isAbstract(candidate.getModifiers()) && !candidate.isInterface()) {
                var candidateMethods = candidate.loadClass().getDeclaredMethods();

                for (var candidateMethod : candidateMethods) {
                    if (candidateMethod.isAnnotationPresent(Injectable.class)) {
                        try {
                            candidateMethod.setAccessible(true);
                            var params = candidateMethod.getParameterTypes();
                            var paramValues = new Object[params.length];

                            for (int i = 0; i < params.length; i++) {
                                paramValues[i] = resolve(params[i]);

                                if (isNull(paramValues[i])) {
                                    throw new IllegalArgumentException("Parameter " + params[i] + " can not be injected...");
                                }
                            }

                            var object = candidateMethod.invoke(paramValues);

                            if (isNull(object)) {
                                throw new IllegalArgumentException("Method " + candidateMethod.getName() + " is not injectable...");
                            }

                            register(object.getClass(), object);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
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

            for (var inter : candidate.getInterfaces()) {
                if (!inter.getPackageName().startsWith("java")) {
                    register(inter, instance);
                }
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

    private <T> T getInjectable(Class<T> beanClass) {
        T injectable = (T) objects.get(beanClass);
        if (injectable == null) {
            createInjectable(beanClass);
            injectable = (T) objects.get(beanClass);
        }
        return injectable;
    }

    @Override
    public Object resolve(Class<?> aClass) {
        return objects.get(aClass);
    }

    @Override
    public void register(Class<?> aClass, Object o) {
        objects.put(aClass, o);
    }

    @Override
    public List<Object> resolveAll() {
        return objects.values().stream().toList();
    }

}

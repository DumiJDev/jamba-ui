package io.github.dumijdev.jambaui.context.container;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;
import io.github.dumijdev.jambaui.core.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public class ApplicationContainer implements IoCContainer<Class<?>, Object> {
    private final Map<Class<?>, Object> objects = new HashMap<>();
    private static final ApplicationContainer instance = new ApplicationContainer();
    private final Logger logger = LoggerFactory.getLogger(ApplicationContainer.class);
    private final Set<Class<?>> inProgress = new HashSet<>();  // Para verificação de dependências circulares

    private ApplicationContainer() {
        objects.put(ApplicationContainer.class, this);
    }

    public static ApplicationContainer getInstance() {
        return instance;
    }

    private boolean isConcreteClasses(Class<?> clazz) {

        var isConcrete = !(clazz.isAnnotation() || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()));

        return isConcrete && !objects.containsKey(clazz);
    }

    @Override
    public void registerFromBase(Class<?> rootClass) {
        try (var result = new ClassGraph().enableAllInfo().acceptPackages(rootClass.getPackage().getName()).scan()) {
            readDependencies(result);
        } catch (Exception e) {
            logger.error("Failed to scan base package: {}", rootClass.getPackage().getName(), e);
            throw new RuntimeException(e);
        }

        try (var result = new ClassGraph().enableAllInfo().acceptPackages("io.github.dumijdev.jambaui").scan()) {
            readDependencies(result);
        } catch (Exception e) {
            logger.error("Failed to scan package: io.github.dumijdev.jambaui", e);
            throw new RuntimeException(e);
        }
    }

    private void readDependencies(ScanResult result) {
        result.getClassesWithAnnotation(Injectable.class).stream()
                .map(ClassInfo::loadClass)
                .filter(this::isConcreteClasses)
                .forEach(this::createInjectable);

        result.getClassesWithAnnotation(Logic.class).stream()
                .map(ClassInfo::loadClass)
                .filter(this::isConcreteClasses)
                .forEach(this::createInjectable);

        result.getClassesWithAnnotation(Config.class).stream()
                .map(ClassInfo::loadClass)
                .filter(this::isConcreteClasses)
                .forEach(this::processConfigClass);
    }

    private void processConfigClass(Class<?> configClass) {
        for (var method : configClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Injectable.class)) {
                injectConfigMethod(method);
            }
        }
    }

    private void injectConfigMethod(Method method) {
        try {
            method.setAccessible(true);
            Object[] paramValues = resolveParameters(method.getParameters());

            Object object = method.invoke(null, paramValues);
            if (object == null) {
                throw new IllegalArgumentException("Method " + method.getName() + " returned null.");
            }

            register(object.getClass(), object);
        } catch (Exception e) {
            logger.error("Failed to inject method: {}", method.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private Object[] resolveParameters(Parameter[] parameters) {
        Object[] paramValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(Property.class)) {
                var property = parameters[i].getAnnotation(Property.class);
                paramValues[i] = PropertyResolver.resolve(property.value(), parameters[i].getType());
            } else {
                paramValues[i] = resolveWithFallback(parameters[i].getType());
            }
        }

        return paramValues;
    }

    // Resolver com fallback para dependências ausentes
    private Object resolveWithFallback(Class<?> type) {
        Object resolved = resolve(type);
        if (resolved == null) {
            logger.warn("Fallback for missing dependency: {}", type.getName());
        }
        return resolved;
    }

    private void createInjectable(Class<?> candidate) {
        try {
            Object instance = createInstance(candidate);
            injectFields(instance);
            register(candidate, instance);
        } catch (Exception e) {
            logger.error("Failed to create injectable for class: {}", candidate.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        if (inProgress.contains(clazz)) {
            throw new RuntimeException("Circular dependency detected with class: " + clazz.getName());
        }

        inProgress.add(clazz); // Marcar como em progresso

        Constructor<?> injectConstructor = findInjectConstructor(clazz);

        Object instance;

        if (injectConstructor != null) {
            Object[] parameterValues = resolveParameters(injectConstructor.getParameters());
            instance = injectConstructor.newInstance(parameterValues);
        } else {
            instance = clazz.getDeclaredConstructor().newInstance();
        }

        inProgress.remove(clazz);

        return instance;
    }

    private Constructor<?> findInjectConstructor(Class<?> clazz) {
        for (var constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return null;
    }

    private void injectFields(Object instance) {
        for (var field : instance.getClass().getDeclaredFields()) {
            if (field.trySetAccessible()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    injectField(instance, field);
                } else if (field.isAnnotationPresent(Property.class)) {
                    injectProperty(instance, field);
                }
            }
        }
    }

    private void injectField(Object instance, Field field) {
        try {
            Object dependency = resolveWithFallback(field.getType());
            if (dependency == null) {
                logger.warn("Field {} could not be injected. Fallback to null.", field.getName());
            }
            field.set(instance, dependency);
        } catch (IllegalAccessException e) {
            logger.error("Failed to inject field: {}", field.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private void injectProperty(Object instance, Field field) {
        try {
            Property property = field.getAnnotation(Property.class);
            Object value = PropertyResolver.resolve(property.value(), field.getType());
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            logger.error("Failed to inject property: {}", field.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private void registerInterfaces(Class<?> clazz, Object instance) {
        for (var iface : clazz.getInterfaces()) {
            if (!iface.getPackageName().startsWith("java")) {
                register(iface, instance);
            }
        }
    }

    @Override
    public Object resolve(Class<?> aClass) {
        var instance = objects.get(aClass);

        if (instance == null && !inProgress.contains(aClass)) {  // Evitar ciclo ao tentar resolver durante a criação
            registerFromBase(aClass);
        }

        inProgress.remove(aClass);  // Remover da lista de dependências em progresso após a resolução

        return instance;
    }

    @Override
    public void register(Class<?> aClass, Object o) {

        if (!aClass.isInterface()) registerInterfaces(aClass, o);

        objects.put(aClass, o);
    }

    @Override
    public List<Object> resolveAll() {
        return new LinkedList<>(objects.values());
    }
}

package io.github.dumijdev.jambaui.context.container;

import io.github.dumijdev.jambaui.context.scanner.ApplicationScanner;
import io.github.dumijdev.jambaui.context.utils.PropertyResolver;
import io.github.dumijdev.jambaui.core.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApplicationContainer implements IoCContainer<Class<?>, Object> {
    private final Map<Class<?>, Object> objects = new ConcurrentHashMap<>();
    private static final List<Class<? extends Annotation>> annotations = new CopyOnWriteArrayList<>();
    private static final ApplicationContainer instance = new ApplicationContainer();
    private final Logger logger = LoggerFactory.getLogger(ApplicationContainer.class);
    private final Set<Class<?>> inProgress = new HashSet<>();

    private ApplicationContainer() {
        registerAnnotation(Injectable.class);

        objects.put(ApplicationContainer.class, this);
    }

    public static void registerAnnotation(final Class<? extends Annotation> annotation) {
        annotations.add(annotation);
    }

    public static ApplicationContainer getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerFromBase(Class<?> rootClass) {
        var basePackages = new String[]{"io.github.dumijdev.jambaui", rootClass.getPackage().getName()};

        var scanner = ApplicationScanner.getInstance();

        logger.info("Starting scanning classes...");
        scanner.scanPackages(basePackages);
        logger.info("Finished scanning classes.");

        scanner.getAnnotationsAnnotatedWith(Injectable.class)
                .forEach(annotation -> annotations.add((Class<? extends Annotation>) annotation));

        annotations.stream().map(scanner::getClassesAnnotatedBy)
                .flatMap(Collection::stream)
                .forEach(this::createInjectable);

        scanner.getClassesAnnotatedBy(Configuration.class)
                .forEach(this::processConfigClass);

        annotations.stream().map(scanner::getInterfacesAnnotatedBy)
                .flatMap(Collection::stream)
                .forEach(this::createInterfaceInjectable);

        scanner.getClassesAnnotatedBy(ExceptionController.class)
                .forEach(this::registerHandlers);
    }

    private void createInterfaceInjectable(Class<?> candidate) {
        var scanner = ApplicationScanner.getInstance();

        scanner.getClassesImplementingInterface(candidate)
                .forEach(this::createInjectable);
    }

    private void registerHandlers(Class<?> candidate) {
        ExceptionHandlerContainer
                .getInstance()
                .registerExceptionHandlers(resolve(candidate));
    }

    private void processConfigClass(Class<?> configClass) {
        for (var method : configClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Injectable.class)) {
                var instance = resolve(configClass);
                injectConfigMethod(instance, method);
            }
        }
    }

    private void injectConfigMethod(Object o, Method method) {
        try {
            method.setAccessible(true);
            var parameters = method.getParameters();

            Object[] paramValues = resolveParameters(parameters);
            var object = method.invoke(o, paramValues);

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
        Constructor<?> injectConstructor = null;
        for (var constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                injectConstructor = constructor;
            }
        }

        if (injectConstructor == null) {
            for (var constructor : clazz.getDeclaredConstructors()) {
                injectConstructor = constructor;

                if (injectConstructor != null) {
                    break;
                }
            }
        }

        return injectConstructor;
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
            if (value != null) {
                field.set(instance, value);
            }
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

        if (instance == null && !inProgress.contains(aClass)) {
            System.out.println(aClass.getName());
            registerFromBase(aClass);
            instance = objects.get(aClass);
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

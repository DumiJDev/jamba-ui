package io.github.dumijdev.jambaui.ioc.container;

import io.github.dumijdev.jambaui.core.layouts.Layout;
import io.github.dumijdev.jambaui.ioc.annotations.Inject;
import io.github.dumijdev.jambaui.ioc.annotations.OnCreated;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutContainer implements IoCContainer<Class<? extends Layout<?>>, Layout<?>> {
    private static final Map<Class<? extends Layout<?>>, Layout<?>> layouts = new HashMap<>();
    private static final LayoutContainer instance = new LayoutContainer();

    public static LayoutContainer getInstance() {
        return instance;
    }

    private LayoutContainer() {
    }

    public void registerFromBase(Class<?> rootClass) {
        var reflections = new Reflections(rootClass);

        for (var type : reflections.getSubTypesOf(Layout.class)) {
            if (!Modifier.isAbstract(type.getModifiers()) && !type.isInterface()) {
                createInjectable((Class<? extends Layout<?>>) type);
            }
        }


    }

    private void createInjectable(Class<? extends Layout<?>> type) {
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

                Class<?>[] parameterTypes = injectConstructor.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters[i] = getInjectable(parameterTypes[i]);
                }

                instance = injectConstructor.newInstance(parameters);
            } else {
                instance = type.getDeclaredConstructor().newInstance();
            }

            register(type, (Layout<?>) instance);
            injectFields(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Layout<?> resolve(Class<? extends Layout<?>> c) {
        return layouts.get(c);
    }

    public void register(Class<? extends Layout<?>> c, Layout<?> layout) {
        layouts.put(c, layout);
    }

    @Override
    public List<Layout<?>> resolveAll() {
        return layouts.values().stream().toList();
    }

    private void injectFields(Object bean) {
        Class<?> beanClass = bean.getClass();
        var fields = beanClass.getDeclaredFields();

        for (var field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                try {
                    field.setAccessible(true);
                    Object dependency = getInjectable(field.getType());
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
}

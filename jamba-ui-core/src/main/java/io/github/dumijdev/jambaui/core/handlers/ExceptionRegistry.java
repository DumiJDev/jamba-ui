package io.github.dumijdev.jambaui.core.handlers;

import io.github.dumijdev.jambaui.core.annotations.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ExceptionRegistry {
    private final Map<Class<? extends Throwable>, Method> handlerMethods = new HashMap<>();
    private final Object handlerInstance;
    private final Logger logger = LoggerFactory.getLogger(ExceptionRegistry.class);

    public ExceptionRegistry(Object handlerInstance) {
        this.handlerInstance = handlerInstance;
        registerHandlers(handlerInstance);
    }

    private void registerHandlers(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExceptionHandler.class)) {
                var exceptionHandler = method.getAnnotation(ExceptionHandler.class);
                handlerMethods.put(exceptionHandler.value(), method);
            }
        }
    }

    // Busca um handler para a exceção, ou retorna null se não encontrar
    public Method getHandler(Class<? extends Throwable> exceptionClass) {
        return handlerMethods.get(exceptionClass);
    }

    // Invoca o handler da exceção
    public void invokeHandler(Throwable exception) {
        var handler = getHandler(exception.getClass());
        if (handler != null) {
            try {
                handler.setAccessible(true);
                handler.invoke(handlerInstance, exception);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("No handler found for {}", exception.getClass().getName());
            logger.error(exception.getMessage());
        }
    }
}


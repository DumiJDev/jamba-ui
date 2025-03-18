package io.github.dumijdev.jambaui.context.container;

import io.github.dumijdev.jambaui.core.handlers.ExceptionRegistry;

import java.util.LinkedList;
import java.util.List;

public class ExceptionHandlerContainer {
    private final List<ExceptionRegistry> exceptionRegistries = new LinkedList<>();
    private static final ExceptionHandlerContainer instance = new ExceptionHandlerContainer();

    private ExceptionHandlerContainer() {}

    public static ExceptionHandlerContainer getInstance() {
        return instance;
    }

    public void registerExceptionHandlers(Object handlerInstance) {
        exceptionRegistries.add(new ExceptionRegistry(handlerInstance));
    }

    public void handleException(Throwable exception) {
        for (ExceptionRegistry registry : exceptionRegistries) {
            registry.invokeHandler(exception);
        }
    }
}

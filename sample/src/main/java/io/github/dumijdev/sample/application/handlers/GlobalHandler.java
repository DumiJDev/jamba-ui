package io.github.dumijdev.sample.application.handlers;

import io.github.dumijdev.jambaui.core.annotations.ExceptionController;
import io.github.dumijdev.jambaui.core.annotations.ExceptionHandler;

@ExceptionController
public class GlobalHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public void handler(IllegalArgumentException ex) {
        System.out.println(ex.getMessage());
    }
}

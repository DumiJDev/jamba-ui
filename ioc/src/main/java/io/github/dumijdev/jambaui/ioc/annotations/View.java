package io.github.dumijdev.jambaui.ioc.annotations;

import io.github.dumijdev.jambaui.core.layouts.Layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
    String value();

    boolean main() default false;

    Class<? extends Layout<?>> layout();
}

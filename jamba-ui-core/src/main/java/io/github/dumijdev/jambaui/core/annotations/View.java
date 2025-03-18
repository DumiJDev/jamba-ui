package io.github.dumijdev.jambaui.core.annotations;

import io.github.dumijdev.jambaui.core.layouts.Layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
    String value();

    String title() default "";

    boolean main() default false;

    Class<? extends Layout<?>> layout();
}

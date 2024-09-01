package io.github.dumijdev.jambaui.desktop;

import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;
import io.github.dumijdev.jambaui.ioc.annotations.View;

@View(value = "main", main = true, layout = HorizontalLayout.class)
public class MainLayout extends Button {
    public MainLayout() {
        super("Olá");
    }
}

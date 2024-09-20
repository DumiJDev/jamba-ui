package com.test;

import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Property;
import io.github.dumijdev.jambaui.core.annotations.View;
import io.github.dumijdev.jambaui.core.utils.Navigator;
import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.components.TextField;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;

@View(value = "main", title = "Main", main = true, layout = HorizontalLayout.class)
public class MainLayout extends HorizontalLayout {
    private final String[] colors = {"red", "blue", "white"};
    private int current = 0;

    @Inject
    public MainLayout(Navigator navigator, @Property("${app.name}") String appName) {
        var bt = new Button("Click " + appName, e -> {
            navigator.navigateTo("main 2");
        });

        bt.getStyle().setBackgroundColor("red").setFontColor("blue");

        add(new TextField("Olá!1"), new TextField("Olá!2"), bt, bt);
    }
}

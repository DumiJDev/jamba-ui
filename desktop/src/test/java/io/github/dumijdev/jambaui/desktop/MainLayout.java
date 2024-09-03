package io.github.dumijdev.jambaui.desktop;

import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;
import io.github.dumijdev.jambaui.desktop.utils.Navigator;
import io.github.dumijdev.jambaui.ioc.annotations.Inject;
import io.github.dumijdev.jambaui.ioc.annotations.View;

@View(value = "main", main = true, layout = HorizontalLayout.class)
public class MainLayout extends HorizontalLayout {
    @Inject
    public MainLayout(Navigator navigator) {
        var bt = new Button("Hello World", e -> {
            System.out.println("Hello World Clicked");
            navigator.navigate("main 2");
        });

        add(bt);
    }
}

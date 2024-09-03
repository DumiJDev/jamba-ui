package io.github.dumijdev.jambaui.desktop;

import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.components.Label;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;
import io.github.dumijdev.jambaui.desktop.utils.Navigator;
import io.github.dumijdev.jambaui.ioc.annotations.Inject;
import io.github.dumijdev.jambaui.ioc.annotations.OnCreated;
import io.github.dumijdev.jambaui.ioc.annotations.View;

@View(value = "main 2", layout = HorizontalLayout.class)
public class Main2Layout extends HorizontalLayout {

    @Inject
    public Main2Layout(Navigator navigator) {
        add(new Button("Hello World 2", e -> {
            navigator.navigate("main");
        }));
    }
}

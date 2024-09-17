package io.github.dumijdev.jambaui.desktop;

import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.Property;
import io.github.dumijdev.jambaui.core.annotations.View;
import io.github.dumijdev.jambaui.core.utils.Navigator;
import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;

@View(value = "main", title = "Main", main = true, layout = HorizontalLayout.class)
public class MainLayout extends HorizontalLayout {
    private final String[] colors = {"red", "blue", "white"};
    private int current = 0;

    @Inject
    public MainLayout(Navigator navigator, @Property("${app.name}") String appName) {
        var bt = new Button("Click " + appName, e -> {
            var bt1 = (Button) e.getSource();

            bt1.getStyle().setBackgroundColor(colors[(current++) % 3])
                    .setFontColor(colors[(1 + current++) % 3]);

        });

        bt.getStyle().setBackgroundColor("red").setFontColor("blue");

        add(bt);
    }
}

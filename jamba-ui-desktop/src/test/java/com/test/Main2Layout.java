package com.test;

import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.components.Grid;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;
import io.github.dumijdev.jambaui.core.utils.Navigator;
import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.View;

@View(value = "main 2", layout = HorizontalLayout.class)
public class Main2Layout extends HorizontalLayout {

    @Inject
    public Main2Layout(Navigator navigator) {
        add(new Button("Hello World 2", e -> navigator.navigateTo("main")));

        var grid = new Grid<String>(true);

        grid.setItems("Paulo");

        grid.addColumn().name("Name").value(s -> s);


        add(grid);
    }
}

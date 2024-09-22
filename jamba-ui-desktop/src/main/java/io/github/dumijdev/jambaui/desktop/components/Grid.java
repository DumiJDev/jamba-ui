package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.components.AbstractTable;
import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.desktop.components.grid.GridColumn;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableView;

import static javafx.collections.FXCollections.observableArrayList;

public class Grid<T> extends AbstractTable<T, MFXTableView<T>> {
    private final MFXTableView<T> tableView;
    private final Style style;

    public Grid() {
        this(false);
    }

    public Grid(boolean pageable) {
        tableView = pageable ? new MFXPaginatedTableView<>() : new MFXTableView<>();

        style = new ComponentStyle(tableView);
    }

    @Override
    public GridColumn<T> addColumn() {
        return new GridColumn<>(tableView);
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public void update() {
        tableView.update();
    }

    @Override
    public MFXTableView<T> getInternal() {
        return tableView;
    }

    @Override
    public void addClassName(String className) {
        tableView.getStyleClass().add(className);
    }

    @Override
    public void removeClassName(String className) {
        tableView.getStyleClass().remove(className);
    }

    @SafeVarargs
    @Override
    public final void setItems(T... items) {
        tableView.setItems(observableArrayList(items));
    }
}

package io.github.dumijdev.jambaui.desktop.components;

import io.github.dumijdev.jambaui.core.components.AbstractTable;
import io.github.dumijdev.jambaui.core.style.Style;
import io.github.dumijdev.jambaui.desktop.components.grid.GridColumn;
import io.github.dumijdev.jambaui.desktop.style.ComponentStyle;
import javafx.scene.control.TableView;

import static javafx.collections.FXCollections.observableArrayList;

public class Grid<T> extends AbstractTable<T, TableView<T>> {
    private final TableView<T> tableView;
    private final Style style;

    public Grid() {
        this(false);
    }

    public Grid(boolean pageable) {
        tableView = new TableView<>();

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
        tableView.layout();
    }

    @Override
    public TableView<T> getInternal() {
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

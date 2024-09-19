package io.github.dumijdev.jambaui.desktop.components.grid;

import io.github.dumijdev.jambaui.core.components.Column;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

import java.util.function.Function;

public class GridColumn<T> implements Column<T> {
    private final MFXTableColumn<T> column;

    public GridColumn(MFXTableView<T> tableView) {
        column = new MFXTableColumn<>();
        tableView.getTableColumns().add(column);
    }

    @Override
    public String getName() {
        return column.getText();
    }

    @Override
    public double getWidth() {
        return column.getPrefWidth();
    }

    @Override
    public String getValue() {
        return column.getText();
    }

    @Override
    public Column<T> name(String name) {
        column.setText(name);

        return this;
    }

    @Override
    public Column<T> width(int width) {

        column.setPrefWidth(width);

        return this;
    }

    @Override
    public Column<T> value(Function<T, Object> fun) {

        column.setRowCellFactory(t -> new MFXTableRowCell<>(fun));

        return this;
    }
}

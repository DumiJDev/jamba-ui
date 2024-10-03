package io.github.dumijdev.jambaui.desktop.components.grid;

import io.github.dumijdev.jambaui.core.Component;
import io.github.dumijdev.jambaui.core.components.Column;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.function.Function;

public class GridColumn<T> implements Column<T> {
    private final TableColumn<T, Object> column;

    public GridColumn(TableView<T> tableView) {
        column = new TableColumn<>();
        tableView.getColumns().add(column);
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

        column.setCellValueFactory(t -> new SimpleObjectProperty<>(fun.apply(t.getValue())));

        return this;
    }

    @Override
    public Column<T> action(Function<T, ? extends Component<?>> fun) {

        column.setCellFactory(t ->  new TableCell<T, Object>(){

            @Override
            protected void updateItem(Object o, boolean b) {
                super.updateItem(o, b);

                if (b) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null);

                    T t1 = getTableView().getItems().get(getIndex());

                    t.setGraphic((Node) fun.apply(t1).getInternal());
                }
            }
        });

        return this;
    }
}

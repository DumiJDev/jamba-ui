package io.github.dumijdev.sample.application.views;

import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.View;
import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.components.Grid;
import io.github.dumijdev.jambaui.desktop.components.Label;
import io.github.dumijdev.jambaui.desktop.components.TextField;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;
import io.github.dumijdev.jambaui.desktop.layouts.VerticalLayout;
import io.github.dumijdev.sample.application.models.Student;
import io.github.dumijdev.sample.application.repository.StudentRepository;

@View(value = "login", title = "${app.name}", main = true, layout = HorizontalLayout.class)
public class MainView extends HorizontalLayout {

    @Inject
    public MainView(StudentRepository repository) {
        var studentName = new TextField("Student name");
        var message = new Label("Message");
        var grid = new Grid<Student>();

        grid.addColumn().name("ID").value(Student::getId).width(100);
        grid.addColumn().name("Name").value(Student::getName).width(100);
        grid.addColumn().name("Age").value(Student::getAge).width(100);
        grid.addColumn().name("Action").action(student -> new Button("Hello")).width(100);

        var vLayout = new VerticalLayout();
        var vLayoutGrid = new VerticalLayout();

        vLayoutGrid.add(grid);

        vLayout.add(
                message,
                studentName,
                new Button("Create", event -> {
                    repository.insert(new Student(studentName.getValue(), 25));

                    message.setText("Student created");
                }),
                new Button("Query All", event -> {
                    var classe = repository.findAll().toList();
                    var students = new Student[classe.size()];

                    grid.setItems(classe.toArray(students));
                })
        );

        add(vLayout, vLayoutGrid);
    }
}

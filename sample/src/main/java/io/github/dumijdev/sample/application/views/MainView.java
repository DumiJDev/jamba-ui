package io.github.dumijdev.sample.application.views;

import io.github.dumijdev.jambaui.core.annotations.Inject;
import io.github.dumijdev.jambaui.core.annotations.View;
import io.github.dumijdev.jambaui.desktop.components.Button;
import io.github.dumijdev.jambaui.desktop.components.Label;
import io.github.dumijdev.jambaui.desktop.components.TextField;
import io.github.dumijdev.jambaui.desktop.layouts.HorizontalLayout;
import io.github.dumijdev.jambaui.desktop.layouts.VerticalLayout;
import io.github.dumijdev.sample.application.models.Student;
import io.github.dumijdev.sample.application.repository.StudentRepository;

@View(value = "login", title = "${app.name}", main = true, layout = HorizontalLayout.class)
public class MainView extends VerticalLayout {

    @Inject
    public MainView(StudentRepository repository) {
        var lastId = new TextField("Last id");
        var studentName = new TextField("Student name");
        var message = new Label("Message");

        add(
                message,
                lastId,
                studentName,
                new Button("Create", event -> {
                    var newStudent = repository.insert(new Student(studentName.getValue(), 25));

                    message.setText("Student created");
                    lastId.setValue(newStudent.getId() + "");
                }),
                new Button("Query", event -> {
                    var id = Integer.valueOf(lastId.getValue());
                    System.out.println("Id: " + id);
                    repository.findById(id).ifPresent(student -> {
                        System.out.println("Student: " + student.getName());
                        message.setText("Student found: " + student.getName());
                    });

                })
        );
    }
}

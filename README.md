# Jamba UI

**Jamba UI** is a modular Java-based UI framework built in my free time to empower developers with a lightweight, rapid, and enjoyable way to create desktop applications. Inspired by the simplicity of Spring Boot and the speed of Vaadin, this framework is designed to make UI development fun and hassle-free.

## Overview

Jamba UI is divided into several Maven modules, each focusing on a distinct aspect of the framework:

- **jamba-ui-context**: Manages the application context, dependency injection, and component scanning. It includes containers like `ApplicationContainer`, `IoCContainer`, and `ViewContainer` to oversee lifecycle management and configuration.
- **jamba-ui-core**: Contains core UI functionalities such as abstract components (buttons, labels, tables, text fields), an event bus system for event management, layout managers, themes, and style support. Annotations (e.g., `@Inject`, `@OnCreated`, `@Configuration`) are used for ease of configuration.
- **jamba-ui-data**: Provides configuration support and data handling, enabling smooth integration with external data sources.
- **jamba-ui-desktop**: Implements desktop-specific components and layouts. It offers ready-to-use components (e.g., `Button`, `Grid`, `Label`, `TextField`) and layout managers (e.g., `AppLayout`, `HorizontalLayout`, `VerticalLayout`) optimized for desktop applications.
- **jamba-ui-security**: (Planned) Handles security aspects like authentication and authorization.
- **Sample**: A sample application that demonstrates how to integrate and use the various modules, serving as an inspiration and reference for developers.

## Motivation

Jamba UI was born from the desire to bring together two great worlds:
- **Simplicity of Spring Boot**: Minimal configuration and a focus on developer productivity.
- **Speed of Vaadin**: Fast, intuitive, and rich UI capabilities out of the box.

This framework is a labor-of-love built in my spare time, and it's all about making desktop application development as delightful and straightforward as possible. Whether you’re prototyping a new idea or developing an enterprise solution, Jamba UI provides an enjoyable, modular, and efficient environment for building your application.

## Features

- **Modular Architecture**: Decoupled modules for context management, core UI, data integration, desktop UI, and security.
- **Dependency Injection & Context Management**: Seamlessly manage components and services with built-in IoC and component scanning.
- **Rich UI Components**: Quickly build interfaces with a mix of abstract and ready-to-use UI components.
- **Flexible Layouts & Styling**: Utilize customizable layouts and themes to match your application’s needs.
- **Event-Driven**: An integrated event system with an event bus for handling interactions dynamically.
- **Easy Customization**: Extend or override default behaviors using intuitive annotations.
- **Fun & Free Time Project**: Developed as a passion project, it’s built with joy and care, aiming to bring simplicity and speed to your development workflow.

## Installation

### Prerequisites

- **Java 17 or later**: The project is built using JDK 17.
- **Maven**: Used for build and dependency management.

### Steps

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/DumiJDev/jamba-ui.git
   ```

2. **Checkout the Development Branch:**

   ```bash
   cd jamba-ui
   git checkout dev
   ```

3. **Build the Project with Maven:**

   ```bash
   mvn clean install
   ```

## Usage

Jamba UI is designed to integrate seamlessly into your desktop applications. Here’s how to get started:

1. **Initialize the Application Context:**

   Begin by initializing the application context from the `jamba-ui-context` module. This setup will automatically scan for UI components and handle dependency injection.

2. **Create and Customize UI Components:**

   Use the abstract components from the `jamba-ui-core` module to design your screens. For example, extend `AbstractButton` or `AbstractLabel` to create custom UI elements.

3. **Manage Layouts and Themes:**

   Utilize layout managers (like `AppLayout`, `HorizontalLayout`, and `VerticalLayout`) provided in the `jamba-ui-desktop` module. Easily change the look and feel by customizing themes via the `Theme` and `Style` classes.

4. **Handle Events:**

   Register event listeners with the integrated event system. Publish and subscribe to events (e.g., `ClickEvent`, `ChangeValueEvent`) using the `EventBus`.

## Examples Based on the Sample

The **Sample** module provides practical examples that show how to use Jamba UI in a real-world scenario. Here are a few highlights:

### Sample Application Structure

- **SampleApplication.java**: The entry point of the sample application, which bootstraps the context and launches the desktop UI.
- **MainView.java**: Demonstrates how to define a view with a layout, add UI components, and handle user interactions.

#### Example Snippet: MainView

```java
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

@View(value = "main", title = "${app.name}", main = true, layout = HorizontalLayout.class)
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
```

#### Example Snippet: Student Repository and Model

The sample also includes a simple demonstration of using a repository to manage data.

```java
// Student.java
package io.github.dumijdev.sample.application.models;

public class Student {
    private String id;
    private String name;
    
    // Constructors, getters, and setters...
}
```

```java
package io.github.dumijdev.sample.application.repository;

import io.github.dumijdev.sample.application.models.Student;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
}
```

These examples show how Jamba UI can be used to build practical, interactive, and data-driven desktop applications in a way that feels both modern and intuitive.

## Use Cases

Jamba UI can be used in a variety of scenarios, including:

- **Enterprise Desktop Applications**: Develop maintainable, enterprise-level desktop applications with built-in dependency injection and an event-driven architecture.
- **Rapid Prototyping**: Quickly prototype data-driven applications with pre-built components and flexible layouts.
- **Educational Projects**: Learn modern UI patterns and modular design principles with clear, structured examples.
- **Hobby & Personal Projects**: Enjoy building desktop applications in your spare time with a framework that values simplicity and speed.
- **Integration with Existing Systems**: Combine the data and security modules to seamlessly integrate with legacy systems or external data sources.

## Contributing

Contributions are always welcome! To contribute:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes and open a pull request.
4. Follow the existing code style and include tests where applicable.

## License

This project is distributed under the [MIT License](LICENSE) – see the LICENSE file for more details.

## Contact

If you have any questions or suggestions, please open an issue on the repository or reach out directly. Happy coding!

---

This README aims to be both informative and cheerful, reflecting the passion and simplicity behind Jamba UI—a framework built to combine the best of Spring Boot’s simplicity with Vaadin’s speed. Enjoy building with Jamba UI!

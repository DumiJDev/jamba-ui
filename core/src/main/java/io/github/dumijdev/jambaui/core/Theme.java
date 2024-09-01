package io.github.dumijdev.jambaui.core;

public abstract class Theme {
    private String themeName;

    public Theme(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeName() {
        return themeName;
    }

    public void applyTo(Component component) {
        // Aplicar o tema ao componente
    }
}

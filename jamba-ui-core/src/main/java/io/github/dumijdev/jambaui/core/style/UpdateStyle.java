package io.github.dumijdev.jambaui.core.style;

@FunctionalInterface
public interface UpdateStyle<IC> {
    void updateStyle(IC ic, Style style);
}

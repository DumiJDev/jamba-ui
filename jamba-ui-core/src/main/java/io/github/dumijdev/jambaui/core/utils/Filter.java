package io.github.dumijdev.jambaui.core.utils;

public interface Filter {
    void doFilter(NavigationContext context, FilterChain chain);
}

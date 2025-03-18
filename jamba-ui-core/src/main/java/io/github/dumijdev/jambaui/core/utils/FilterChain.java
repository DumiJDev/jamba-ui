package io.github.dumijdev.jambaui.core.utils;

import java.util.List;

public class FilterChain {

    private final List<Filter> filters;
    private int currentPosition = 0;

    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    public void doFilter(NavigationContext context) {
        if (currentPosition < filters.size()) {
            Filter currentFilter = filters.get(currentPosition++);
            currentFilter.doFilter(context, this);
        }
    }
}

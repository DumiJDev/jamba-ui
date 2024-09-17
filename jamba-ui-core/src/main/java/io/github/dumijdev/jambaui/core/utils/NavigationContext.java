package io.github.dumijdev.jambaui.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NavigationContext {
    private static NavigationContext instance;
    private String fromView;
    private String toView;
    private final Map<String, Object> attributes;

    private NavigationContext() {
        attributes = new HashMap<>();
    }

    public static NavigationContext getInstance() {
        if (instance == null) {
            instance = new NavigationContext();
        }

        return instance;
    }

    public String getFromView() {
        return fromView;
    }

    public void setFromView(String fromView) {
        this.fromView = fromView;
    }

    public String getToView() {
        return toView;
    }

    public void setToView(String toView) {
        this.toView = toView;
    }

    public Set<Map.Entry<String, Object>> getAttributes() {
        return attributes.entrySet();
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}

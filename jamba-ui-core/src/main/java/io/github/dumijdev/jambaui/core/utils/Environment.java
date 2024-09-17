package io.github.dumijdev.jambaui.core.utils;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, String> env = new HashMap<String, String>();

    public String getProperty(String key) {
        return env.get(key);
    }

    public void setProperty(String key, String value) {
        env.put(key, value);
    }
}

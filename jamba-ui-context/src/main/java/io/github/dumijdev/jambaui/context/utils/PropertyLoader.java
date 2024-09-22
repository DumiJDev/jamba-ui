package io.github.dumijdev.jambaui.context.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertyLoader {
    private final Properties properties = new Properties();

    public PropertyLoader(InputStream input) {
        try {
            if (input == null) {
                return;
            }
            properties.load(input);
        } catch (IOException e) {}
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Set<Map.Entry<String, Object>> getProperties() {
        return properties.entrySet().stream().map(objectObjectEntry -> new Map.Entry<String, Object>() {
            @Override
            public String getKey() {
                return objectObjectEntry.getKey().toString();
            }

            @Override
            public Object getValue() {
                return objectObjectEntry.getValue();
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        }).collect(Collectors.toSet());
    }
}


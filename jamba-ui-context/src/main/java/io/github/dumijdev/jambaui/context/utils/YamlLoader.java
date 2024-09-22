package io.github.dumijdev.jambaui.context.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class YamlLoader {
    private Properties properties;

    public YamlLoader(InputStream input) {
        try {
            if (input == null) {
                return;
            }
            Yaml yaml = new Yaml();

            properties = convertToProperties(yaml.load(input));
        } catch (Exception e) {

        }
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Set<Map.Entry<String, Object>> getProperties() {
        return properties.entrySet()
                .stream()
                .map(objectObjectEntry -> Map.entry(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue())).collect(Collectors.toSet());
    }

    private static Properties convertToProperties(Map<String, Object> map) {
        Properties properties = new Properties();
        buildPropertiesFromMap("", map, properties);
        return properties;
    }

    private static void buildPropertiesFromMap(String prefix, Map<String, Object> map, Properties properties) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (prefix.isEmpty() ? "" : prefix + ".") + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                buildPropertiesFromMap(key, (Map<String, Object>) value, properties);
            } else {
                properties.put(key, value.toString());
            }
        }
    }
}

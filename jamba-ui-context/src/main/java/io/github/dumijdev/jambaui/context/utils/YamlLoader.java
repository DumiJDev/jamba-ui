package io.github.dumijdev.jambaui.context.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class YamlLoader {
    private Map<String, Object> yamlData;

    public YamlLoader(String fileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                return;
            }
            Yaml yaml = new Yaml();
            yamlData = yaml.load(input);
        } catch (Exception e) {

        }
    }

    public Object getProperty(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> data = yamlData;
        for (String k : keys) {
            Object value = data.get(k);
            if (value instanceof Map) {
                data = (Map<String, Object>) value;
            } else {
                return value;
            }
        }
        return null;
    }

    public Set<Map.Entry<String, Object>> getProperties() {
        return yamlData.entrySet();
    }
}
